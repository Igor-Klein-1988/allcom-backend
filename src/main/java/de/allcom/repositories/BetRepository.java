package de.allcom.repositories;

import de.allcom.models.Bet;
import de.allcom.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByAuctionId(Long auctionId);

    Optional<Bet> findFirstByAuctionIdOrderByBetAmountDesc(Long auctionId);

    Page<Bet> findAllByUser(User user, Pageable pageable);
}
