package de.allcom.repositories;

import de.allcom.models.Auction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findAuctionsByState(Auction.State state);
}
