package de.allcom.repositories;

import de.allcom.models.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findFirstByProductIdOrderByCreatedAtDesc(Long productId);

    List<Auction> findAllByProductId(Long productId);
}
