package de.allcom.repositories;

import de.allcom.models.Product;
import de.allcom.models.Wishlist;
import de.allcom.models.WishlistItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findAllByWishlistId(Long wishlistId);

    void deleteByProductAndWishlist(Product product, Wishlist wishlist);

    Optional<WishlistItem> findByWishlistAndProduct(Wishlist wishlist, Product product);
}
