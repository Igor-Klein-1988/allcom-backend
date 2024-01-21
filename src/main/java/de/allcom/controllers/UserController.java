package de.allcom.controllers;

import de.allcom.controllers.api.UsersApi;
import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.dto.user.UserAddressResponseDto;
import de.allcom.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController implements UsersApi {
    private final UserService userService;

    @Override
    public ResponseEntity<List<UserAddressResponseDto>> getAll(int limit, int skip) {
        return ResponseEntity.ok(userService.getAll(limit, skip));
    }

    @Override
    public ResponseEntity<UserAddressResponseDto> updateUser(UserAddressRegistrationDto request, Long userId) {
        return ResponseEntity.ok(userService.updateUser(request, userId));
    }

    @Override
    public ResponseEntity<UserAddressResponseDto> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @Override
    public ResponseEntity<UserAddressResponseDto> foundUserByEmail(String userEmail) {
        return ResponseEntity.ok(userService.foundUserByEmail(userEmail));
    }

    @Override
    public ResponseEntity<UserAddressResponseDto> foundUserById(Long userId) {
        return ResponseEntity.ok(userService.foundUserById(userId));
    }

    @Override
    public ResponseEntity<UserAddressResponseDto> changeStatus(Long userId, String status) {
        return ResponseEntity.ok(userService.changeStatus(userId, status));
    }
}
