package de.allcom.services;

import de.allcom.dto.product.ProductWishlistDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import de.allcom.models.Wishlist;
import de.allcom.models.WishlistItem;
import de.allcom.repositories.ProductRepository;
import de.allcom.repositories.WishlistItemRepository;
import de.allcom.repositories.WishlistRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final WishlistItemService wishlistItemService;

    public List<ProductWishlistDto> findProducts(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId);
        if (wishlist != null) {
            List<WishlistItem> wishlistItems = wishlistItemRepository.findAllByWishlistId(wishlist.getId());
            List<Product> wishProducts = wishlistItems.stream()
                    .map(WishlistItem::getProduct)
                    .toList();
            return wishProducts.stream()
                    .map(p -> ProductWishlistDto.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .description(p.getDescription())
                            .weight(p.getWeight())
                            .color(p.getColor())
                            .categoryId(p.getCategory().getId())
                            .state(p.getState().name())
                            .imageLinks(p.getImages().stream().map(ProductImage::getLink).toList())
                            .build())
                    .toList();
        } else {
            return null;
        }
    }

    public List<ProductWishlistDto> addProduct(Long userId, Long productId) {
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
        return findProducts(userId);
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
