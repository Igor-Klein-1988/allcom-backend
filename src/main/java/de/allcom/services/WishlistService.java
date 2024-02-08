package de.allcom.services;

import de.allcom.dto.product.ProductResponseDto;
import de.allcom.dto.product.ProductWithAuctionDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Product;
import de.allcom.models.Wishlist;
import de.allcom.models.WishlistItem;
import de.allcom.repositories.ProductRepository;
import de.allcom.repositories.WishlistItemRepository;
import de.allcom.repositories.WishlistRepository;
import de.allcom.services.utils.Converters;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final Converters converters;

    public Page<ProductWithAuctionDto> findProducts(Long userId, PageRequest pageRequest) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId);
        if (wishlist != null) {
            Page<WishlistItem> wishlistItems =
                    wishlistItemRepository.findAllByWishlistId(wishlist.getId(), pageRequest);
            Page<ProductResponseDto> productResponseDto = wishlistItems
                    .map(wi -> converters.convertToProductResponseDto(wi.getProduct(), null));

            return productResponseDto.map(p -> ProductWithAuctionDto.builder()
                                                                    .id(p.getId())
                                                                    .name(p.getName())
                                                                    .description(p.getDescription())
                                                                    .weight(p.getWeight())
                                                                    .color(p.getColor())
                                                                    .categoryId(p.getCategoryId())
                                                                    .state(p.getState())
                                                                    .imageLinks(p.getImageLinks())
                                                                    .lastCreatedAuction(p.getLastCreatedAuction())
                                                                    .build());
        } else {
            return null;
        }
    }

    public Page<ProductWithAuctionDto> addProduct(Long userId, Long productId, PageRequest pageRequest) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId);
        Product productForAdd = productRepository.findById(productId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Product with id: " + productId + " not found"));
        if (wishlistItemRepository.findByWishlistAndProduct(wishlist, productForAdd).isPresent()) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Product with id: "
                    + productId + " in your wishlist");
        }
        WishlistItem newItem = new WishlistItem();
        newItem.setWishlist(wishlist);
        newItem.setProduct(productForAdd);
        newItem.setAddedAt(LocalDateTime.now());
        wishlistItemRepository.save(newItem);
        wishlistRepository.save(wishlist);
        return findProducts(userId, pageRequest);
    }

    @Transactional
    public void removeProduct(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId);
        Product productForRemove = productRepository.findById(productId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Product with id: " + productId + " not found"));
        WishlistItem wishlistForRemove = wishlistItemRepository.findByWishlistAndProduct(wishlist, productForRemove)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Product with id: "
                + productId + " not found in your wishlist"));
        wishlist.getWishlistItems().remove(wishlistForRemove);
        wishlistItemRepository.deleteByProductAndWishlist(productForRemove, wishlist);
        wishlistRepository.save(wishlist);
    }
}
