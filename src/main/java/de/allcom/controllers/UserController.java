package de.allcom.controllers;

import de.allcom.controllers.api.UsersApi;
import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.dto.user.UserAddressResponseDto;
import de.allcom.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UsersApi {
    private final UserService userService;

    @Override
    public Page<UserAddressResponseDto> getAll(int limit, int skip) {
        PageRequest pageRequest = PageRequest.of(skip, limit);
        return userService.getAll(pageRequest);
    }

    @Override
    public UserAddressResponseDto updateUser(UserAddressRegistrationDto request, Long userId) {
        return userService.updateUser(request, userId);
    }

    @Override
    public UserAddressResponseDto getUserProfile() {
        return userService.getUserProfile();
    }

    @Override
    public UserAddressResponseDto foundUserByEmail(String userEmail) {
        return userService.foundUserByEmail(userEmail);
    }

    @Override
    public UserAddressResponseDto foundUserById(Long userId) {
        return userService.foundUserById(userId);
    }

    @Override
    public UserAddressResponseDto changeStatus(Long userId, String status) {
        return userService.changeStatus(userId, status);
    }
}
