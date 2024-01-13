package de.allcom.services;

import de.allcom.dto.AuctionRequestDto;
import de.allcom.dto.AuctionResponseDto;
import de.allcom.dto.NewBetDto;
import de.allcom.exceptions.RestException;
import de.allcom.jobs.MyBackgroundJob;
import de.allcom.jobs.MyJob;
import de.allcom.models.Auction;
import de.allcom.models.Bet;
import de.allcom.models.Product;
import de.allcom.models.User;
import de.allcom.repositories.AuctionRepository;
import de.allcom.repositories.BetRepository;
import de.allcom.repositories.ProductRepository;
import de.allcom.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.quartz.Scheduler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final UsersRepository userRepository;
    private final BetRepository betRepository;
    private final BetService betService;
    private final ProductRepository productRepository;


    private JobScheduler jobScheduler;
    private final MyBackgroundJob myBackgroundJob;

//    public void enqueueTask() {
//        jobScheduler.enqueue(() -> myBackgroundJob
//                .performTask("Привет, мир!"));
//    }

    public void scheduleMyTask() {
        LocalDateTime targetTime = LocalDateTime.now()
                                                .plusSeconds(20);
        ZonedDateTime zonedTargetTime = targetTime.atZone(ZoneId.systemDefault());
        System.out.println("=== === ===");

        jobScheduler.schedule(zonedTargetTime, () -> myBackgroundJob.sayHello("Игорь"));

    }



//    private final Scheduler scheduler;

    @Transactional
    public AuctionResponseDto addBet(NewBetDto newBetDto) {
        User user = userRepository.findById(newBetDto.getUserId())
                                  .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                          "User with id <" + newBetDto.getUserId() + "> not found"));
        System.out.println("==== == BEGIN == ====");
        auctionRepository.findAll()
                         .forEach(System.out::println);
        System.out.println("==== == END == ====");
        Auction auction = auctionRepository.findById(newBetDto.getAuctionId())
                                           .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                   "Auction with id <" + newBetDto.getAuctionId() + "> not found"));

        Optional<Bet> lastBet = betRepository.findFirstByAuctionIdOrderByIdDesc(auction.getId());
        Integer lastBetAmount = lastBet.isPresent() ? lastBet.get()
                                                             .getBetAmount() : auction.getStartPrice();
        if (lastBetAmount >= newBetDto.getBetAmount()) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Bid rejected: the new bet <" + newBetDto.getBetAmount()
                    + "> must be higher than the current highest bet <" + lastBetAmount + ">.");
        }

        betService.createBet(user, auction, newBetDto.getBetAmount());
        scheduleMyTask();

        return AuctionResponseDto.builder()
                                 .id(newBetDto.getAuctionId())
                                 .productId(auction.getProduct()
                                                   .getId())
                                 .number(auction.getNumber())
                                 .lastBetAmount(newBetDto.getBetAmount())
                                 .build();
    }

    public AuctionResponseDto create(AuctionRequestDto request) {
        Product product = productRepository.findById(request.getProductId())
                                           .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                   "Product with id <" + request.getProductId() + "> not found"));
        ;
        Auction newAuction = Auction.builder()
                                    .product(product)
                                    .number(request.getNumber())
                                    .startPrice(request.getStartPrice())
                                    .startAt(request.getStartAt())
                                    .plannedEndAt(request.getPlannedEndAt())
                                    .state(Auction.State.ACTIVE)
                                    .updateAt(LocalDateTime.now())
                                    .createdAt(LocalDateTime.now())
                                    .build();
        Auction savedAuction = auctionRepository.save(newAuction);

//        JobDetail job = newJob(MyJob.class)
//                .withIdentity("email-job-" + savedAuction.getId())
//                .usingJobData("productId", product.getId())
//                .build();
//        Trigger trigger = newTrigger()
//                .withIdentity("trigger-email-job-" + savedAuction.getId())
//                .startAt(futureDate(24, DateBuilder.IntervalUnit.SECOND))
//                .build();
//
//        try {
//            scheduler.scheduleJob(job, trigger);
//        } catch (SchedulerException e) {
//            System.out.println("EEEEE ==== EEEEEE");
//            throw new RuntimeException(e);
//        }

        return AuctionResponseDto.builder()
                                 .isFinished(false)
                                 .id(savedAuction.getId())
                                 .productId(savedAuction.getProduct()
                                                        .getId())
                                 .lastBetAmount(savedAuction.getStartPrice())
                                 .build();
    }
}
