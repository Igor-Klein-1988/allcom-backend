package de.allcom.controllers;

import de.allcom.controllers.api.UsersApi;
import de.allcom.dto.user.UserDto;
import de.allcom.dto.user.UserRegistrationDto;
import de.allcom.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UsersApi {
    private final UsersService usersService;

    @Override
    public UserDto register(UserRegistrationDto newUser) {
        return usersService.register(newUser);
    }
}
