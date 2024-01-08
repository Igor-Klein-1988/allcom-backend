package de.allcom.services;

import de.allcom.dto.user.UserDto;
import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.User;
import de.allcom.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.allcom.dto.user.UserDto.from;


@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public UserDto register(UserAddressRegistrationDto newUser) {
        checkIfExistsByEmail(newUser);

        User user = saveNewUser(newUser);

        return from(user);
    }

    private User saveNewUser(UserAddressRegistrationDto newUser) {
        User user = User.builder()
                        .firstName(newUser.getFirstName())
                        .lastName(newUser.getLastName())
                        .email(newUser.getEmail())
                        .hashPassword(newUser.getPassword())
                        .build();

        usersRepository.save(user);

        return user;
    }

    private void checkIfExistsByEmail(UserAddressRegistrationDto newUser) {
        if (usersRepository.existsByEmail(newUser.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT, "User with email <" + newUser.getEmail() + "> already exists");
        }
    }

    public UserDto getUserById(Long currentUserId) {
        return from(usersRepository.findById(currentUserId)
                                   .orElseThrow());
    }

    public List<UserDto> getAll() {
        return usersRepository.findAll()
                              .stream()
                              .map(UserDto::from)
                              .toList();
    }
}
