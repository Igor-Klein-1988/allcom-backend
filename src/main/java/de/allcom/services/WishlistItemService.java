package de.allcom.services;

import de.allcom.models.Product;
import de.allcom.models.Wishlist;
import de.allcom.repositories.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WishlistItemService {
    private  final WishlistItemRepository wishlistItemRepository;

    public void remove(Wishlist wishlist, Product product) {
        wishlistItemRepository.deleteByProductAndWishlist(product, wishlist);
    }
}
