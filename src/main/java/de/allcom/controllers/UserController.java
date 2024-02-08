package de.allcom.controllers;

import de.allcom.controllers.api.UsersApi;
import de.allcom.dto.product.ProductWishlistDto;
import de.allcom.dto.user.UserWithAddressResponseDto;
import de.allcom.dto.user.UserWithAddressUpdateDto;
import de.allcom.exceptions.RestException;
import de.allcom.services.UserService;
import de.allcom.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UsersApi {
    private final UserService userService;
    private final WishlistService wishlistService;

    @Override
    public Page<UserWithAddressResponseDto> getAll(int limit, int skip, String searchQuery) {
        if (limit < 0 || skip < 0) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Limit and skip must be non-negative values.");
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            return userService.searchUsers(limit, skip, searchQuery);
        } else {
            PageRequest pageRequest = PageRequest.of(skip, limit);
            return userService.getAll(pageRequest);
        }
    }

    @Override
    public UserWithAddressResponseDto updateUser(UserWithAddressUpdateDto request, Long userId) {
        return userService.updateUser(request, userId);
    }

    @Override
    public UserWithAddressResponseDto getUserProfile() {
        return userService.getUserProfile();
    }

    @Override
    public UserWithAddressResponseDto foundUserByEmail(String userEmail) {
        return userService.foundUserByEmail(userEmail);
    }

    @Override
    public UserWithAddressResponseDto foundUserById(Long userId) {
        return userService.foundUserById(userId);
    }

    @Override
    public UserWithAddressResponseDto changeCredentialStatus(Long userId, boolean isChecked) {
        return userService.changeCredentialStatus(userId, isChecked);
    }

    @Override
    public UserWithAddressResponseDto changeBlockedStatus(Long userId, boolean isBlocked) {
        return userService.changeBlockedStatus(userId, isBlocked);
    }

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
