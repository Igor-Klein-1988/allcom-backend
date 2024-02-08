package de.allcom.repositories;

import de.allcom.models.Product;
import de.allcom.models.Wishlist;
import de.allcom.models.WishlistItem;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    Page<WishlistItem> findAllByWishlistId(Long wishlistId, Pageable pageable);

    void deleteByProductAndWishlist(Product product, Wishlist wishlist);

    Optional<WishlistItem> findByWishlistAndProduct(Wishlist wishlist, Product product);
}
