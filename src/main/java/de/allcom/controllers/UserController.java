package de.allcom.controllers;

import de.allcom.controllers.api.UsersApi;
import de.allcom.dto.user.UserWithAddressRegistrationDto;
import de.allcom.dto.user.UserWithAddressResponseDto;
import de.allcom.exceptions.RestException;
import de.allcom.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UsersApi {
    private final UserService userService;

    @Override
    public Page<UserWithAddressResponseDto> getAll(int limit, int skip) {
        if (limit < 0 || skip < 0) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Limit and skip must be non-negative values.");
        }
        PageRequest pageRequest = PageRequest.of(skip, limit);
        return userService.getAll(pageRequest);
    }

    @Override
    public UserWithAddressResponseDto updateUser(UserWithAddressRegistrationDto request, Long userId) {
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
    public UserWithAddressResponseDto changeStatus(Long userId, boolean isChecked, boolean isBlocked) {
        return userService.changeStatus(userId, isChecked, isBlocked);
    }
}
