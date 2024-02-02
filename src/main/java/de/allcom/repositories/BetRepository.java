package de.allcom.repositories;

import de.allcom.models.Bet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByAuctionId(Long auctionId);

    Optional<Bet> findFirstByAuctionIdOrderByBetAmountDesc(Long auctionId);
}
