package de.allcom.controllers;

import de.allcom.controllers.api.WishlistApi;
import de.allcom.dto.product.ProductWishlistDto;
import de.allcom.services.WishlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WishlistController implements WishlistApi {
    private final WishlistService wishlistService;

    @Override
    public List<ProductWishlistDto> findProducts(Long userId) {
        return wishlistService.findProducts(userId);
    }

    @Override
    public List<ProductWishlistDto> addProductToWishlist(Long userId, Long productId) {
        return wishlistService.addProduct(userId, productId);
    }

    @Override
    public void removeProductFromWishlist(Long userId, Long productId) {
        wishlistService.removeProduct(userId, productId);
    }
}
