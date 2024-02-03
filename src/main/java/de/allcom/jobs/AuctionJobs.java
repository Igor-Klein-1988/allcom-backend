package de.allcom.jobs;

import de.allcom.dto.auction.AuctionResponseDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Auction;
import de.allcom.models.Bet;
import de.allcom.repositories.AuctionRepository;
import de.allcom.repositories.BetRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionJobs {

    private final JobScheduler jobScheduler;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionRepository auctionRepository;
    private final BetRepository betRepository;

    @Job(name = "Check Auction State")
    public void checkAuctionState(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                                           .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                   "Auction with id <" + auctionId + "> not found"));
        LocalDateTime now = LocalDateTime.now();
        if (!auction.getCurrentPlannedEndAt().isAfter(now)) {
            List<Bet> bets = betRepository.findByAuctionId(auction.getId());
            Auction.State state = bets.isEmpty() ? Auction.State.UNSUCCESSFUL : Auction.State.SUCCESSFUL;
            closeAuction(auction, state, now);
        } else {
            scheduleNextCheck(auction, auction.getCurrentPlannedEndAt());
        }
    }

    private void scheduleNextCheck(Auction auction, LocalDateTime nextCheckTime) {
        ZonedDateTime zonedTargetTime = nextCheckTime.atZone(ZoneId.systemDefault());
        log.info("Create new Job for auctionId = " + auction.getId());
        jobScheduler.schedule(zonedTargetTime, () -> checkAuctionState(auction.getId()));
    }

    private void closeAuction(Auction auction, Auction.State auctionState, LocalDateTime now) {
        auction.setUpdatedAt(now);
        auction.setActualEndAt(now);
        Auction.State oldState = auction.getState();
        auction.setState(auctionState);
        Auction savedAuction = auctionRepository.save(auction);
        log.info("Change state from <" + oldState + "> to <" + savedAuction.getState() + "> for auctionId = "
                + auction.getId());

        AuctionResponseDto auctionResponseDto = AuctionResponseDto.builder()
                                                                  .id(auction.getId())
                                                                  .productId(auction.getProduct().getId())
                                                                  .currentPlannedEndAt(now)
                                                                  .state(savedAuction.getState())
                                                                  .build();

        messagingTemplate.convertAndSend("/topic/auction/" + savedAuction.getId(), auctionResponseDto);
    }
}
