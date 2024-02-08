package de.allcom.controllers;

import de.allcom.controllers.api.WishlistApi;
import de.allcom.dto.product.ProductWishlistDto;
import de.allcom.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WishlistController implements WishlistApi {
    private final WishlistService wishlistService;

    @Override
    public Page<ProductWishlistDto> findProducts(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return wishlistService.findProducts(userId, pageRequest);
    }

    @Override
    public Page<ProductWishlistDto> addProductToWishlist(Long userId, Long productId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return wishlistService.addProduct(userId, productId, pageRequest);
    }

    @Override
    public void removeProductFromWishlist(Long userId, Long productId) {
        wishlistService.removeProduct(userId, productId);
    }
}
