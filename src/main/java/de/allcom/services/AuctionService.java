package de.allcom.services;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.auction.AuctionDto;
import de.allcom.dto.auction.AuctionRequestDto;
import de.allcom.dto.auction.AuctionResponseDto;
import de.allcom.dto.bet.NewBetDto;
import de.allcom.exceptions.RestException;
import de.allcom.jobs.AuctionJobs;
import de.allcom.models.Auction;
import de.allcom.models.Bet;
import de.allcom.models.Product;
import de.allcom.models.User;
import de.allcom.repositories.AuctionRepository;
import de.allcom.repositories.BetRepository;
import de.allcom.repositories.ProductRepository;
import de.allcom.repositories.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {
    public static final int EXTRA_TIME_AFTER_LAST_BET_SECONDS = 120;

    private final AuctionRepository auctionRepository;
    private final BetRepository betRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BetService betService;

    private final SimpMessagingTemplate messagingTemplate;
    private final JobScheduler jobScheduler;
    private final AuctionJobs auctionJobs;

    private void addAuctionStateCheckerTask(Auction auction) {
        ZonedDateTime zonedTargetTime = auction.getCurrentPlannedEndAt().atZone(ZoneId.systemDefault());
        log.info("Create new Job for auctionId = " + auction.getId());
        jobScheduler.schedule(zonedTargetTime, () -> auctionJobs.checkAuctionState(auction.getId()));
    }

    public Auction create(AuctionRequestDto request, Long productId) {
        Product product = productRepository.findById(productId)
                                           .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                   "Product with id <" + productId + "> not found"));
        validateAuctionDates(request.getStartAt(), request.getPlannedEndAt());

        Auction newAuction = Auction.builder()
                                    .product(product)
                                    .startPrice(request.getStartPrice())
                                    .startAt(request.getStartAt())
                                    .plannedEndAt(request.getPlannedEndAt())
                                    .currentPlannedEndAt(request.getPlannedEndAt())
                                    .state(Auction.State.ACTIVE)
                                    .updatedAt(LocalDateTime.now())
                                    .createdAt(LocalDateTime.now())
                                    .build();
        Auction savedAuction = auctionRepository.save(newAuction);
        addAuctionStateCheckerTask(savedAuction);

        return savedAuction;
    }


    public Auction update(AuctionDto auctionDto) {
        Auction auctionForUpdate = auctionRepository.findById(auctionDto.getId())
                                                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                            "Auction with id: " + auctionDto.getId() + " not found"));
        validateAuctionDates(auctionDto.getStartAt(), auctionDto.getPlannedEndAt());

        auctionForUpdate.setStartPrice(auctionDto.getStartPrice());
        auctionForUpdate.setStartAt(auctionDto.getStartAt());
        auctionForUpdate.setPlannedEndAt(auctionDto.getPlannedEndAt());
        auctionForUpdate.setUpdatedAt(LocalDateTime.now());

        return auctionRepository.save(auctionForUpdate);
    }

    @Transactional
    public void addBet(NewBetDto newBetDto, SimpMessageHeaderAccessor headerAccessor) {
        try {
            log.info(AuctionService.log.getName());
            final User user = userRepository.findById(newBetDto.getUserId())
                                            .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                    "User with id <" + newBetDto.getUserId() + "> not found"));
            if (user.isBlocked() || !user.isChecked()) {
                String errorMessage = "User with id <" + user.getId() + "> Blocked or not Checked";
                throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
            }

            Auction auction = auctionRepository.findById(newBetDto.getAuctionId())
                                               .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                       "Auction with id <" + newBetDto.getAuctionId() + "> not found"));
            if (!auction.getState().equals(Auction.State.ACTIVE)) {
                throw new RestException(HttpStatus.BAD_REQUEST,
                        "Auction with id <" + newBetDto.getAuctionId() + "> has state <" + auction.getState() + ">");
            }

            Optional<Bet> maxBet = betRepository.findFirstByAuctionIdOrderByBetAmountDesc(auction.getId());
            Integer currentPrice = maxBet.isPresent() ? maxBet.get().getBetAmount() : auction.getStartPrice();
            Integer betAmount = newBetDto.getBetAmount();
            if (currentPrice < 0 || betAmount < currentPrice) {
                throw new RestException(HttpStatus.BAD_REQUEST,
                        "Bet rejected for auction id [" + auction.getId() + "]: " + "the new bet amount [" + betAmount
                                + "] must be higher than the current highest price [" + currentPrice + "].");
            }

            int validBet = betService.getValidBet(currentPrice);
            if (validBet != betAmount) {
                throw new RestException(HttpStatus.BAD_REQUEST,
                        "Bet rejected for auction id [" + auction.getId() + "]: the new bet amount [" + betAmount
                                + "] does not match the valid bet amount [" + validBet + "].");
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime checkTime = auction.getCurrentPlannedEndAt().minusSeconds(EXTRA_TIME_AFTER_LAST_BET_SECONDS);
            if (checkTime.isBefore(now)) {
                auction.setCurrentPlannedEndAt(now.plusSeconds(EXTRA_TIME_AFTER_LAST_BET_SECONDS));
                auctionRepository.save(auction);
            }
            betService.createBet(user, auction, betAmount);
            AuctionResponseDto response = AuctionResponseDto.from(auction, betAmount);

            messagingTemplate.convertAndSend("/topic/auction/" + response.getId(), response);
            messagingTemplate.convertAndSend("/topic/auction/info" , response);
        } catch (RestException e) {
            log.error("Error processing bet: " + e.getMessage());
            String username = headerAccessor.getUser().getName();
            Optional<User> user = userRepository.findByEmail(username);
            if (user.isPresent()) {
                StandardResponseDto errorMessage = StandardResponseDto.builder().message(e.getMessage()).build();
                messagingTemplate.convertAndSend("/topic/error/" + user.get().getId(), errorMessage);
            }
        }
    }

    private void validateAuctionDates(LocalDateTime startAt, LocalDateTime plannedEndAt) {
        if (plannedEndAt.isBefore(LocalDateTime.now())) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "The end date of the auction cannot be in the past. Specified end date: " + plannedEndAt + ".");
        }

        if (plannedEndAt.isBefore(startAt)) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "The end date of the auction cannot be earlier than its start date. Start date: " + startAt
                            + ", end date: " + plannedEndAt + ".");
        }
    }
}
