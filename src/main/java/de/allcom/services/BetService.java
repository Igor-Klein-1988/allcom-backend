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
    private static final int BET_INCREMENT_1 = 5;
    private static final int BET_INCREMENT_2 = 10;
    private static final int BET_INCREMENT_3 = 50;
    private static final int BET_INCREMENT_4 = 100;

    private static final int PRICE_THRESHOLD_1 = 100;
    private static final int PRICE_THRESHOLD_2 = 500;
    private static final int PRICE_THRESHOLD_3 = 1000;

    private final BetRepository betRepository;

    public int getValidBet(int currentPrice) {
        int betIncrement;
        if (currentPrice < PRICE_THRESHOLD_1) {
            betIncrement = BET_INCREMENT_1;
        } else if (currentPrice < PRICE_THRESHOLD_2) {
            betIncrement = BET_INCREMENT_2;
        } else if (currentPrice < PRICE_THRESHOLD_3) {
            betIncrement = BET_INCREMENT_3;
        } else {
            betIncrement = BET_INCREMENT_4;
        }

        return currentPrice + betIncrement;
    }

    public Bet createBet(User user, Auction auction, int betAmount) {
        Bet newBet = Bet.builder()
                        .user(user)
                        .auction(auction)
                        .betAmount(betAmount)
                        .createdAt(LocalDateTime.now())
                        .build();

        return betRepository.save(newBet);
    }
}
