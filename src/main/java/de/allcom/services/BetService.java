package de.allcom.services;

import de.allcom.models.Auction;
import de.allcom.models.Bet;
import de.allcom.models.User;
import de.allcom.repositories.BetRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BetService {
    private final BetRepository betRepository;

    public Bet createBet(User user, Auction auction, Integer betAmount) {
        Bet newBet = Bet.builder()
                        .user(user)
                        .auction(auction)
                        .betAmount(betAmount)
                        .createdAt(LocalDateTime.now())
                        .build();

        return betRepository.save(newBet);
    }
}
