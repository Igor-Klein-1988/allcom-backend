package de.allcom.services;

import de.allcom.dto.user.UserDto;
import de.allcom.dto.user.UserRegistrationDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.User;
import de.allcom.repositories.UsersRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static de.allcom.dto.user.UserDto.from;


@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    //    private final PasswordEncoder passwordEncoder;


    public UserDto register(UserRegistrationDto newUser) {
        checkIfExistsByEmail(newUser);

        User user = saveNewUser(newUser);

        return from(user);
    }

    private User saveNewUser(UserRegistrationDto newUser) {
        User user = User.builder()
                        .firstName(newUser.getFirstName())
                        .lastName(newUser.getLastName())
                        .email(newUser.getEmail())
                        //.hashPassword(passwordEncoder.encode(newUser.getPassword()))
                        .hashPassword(newUser.getPassword())
                        .build();

        usersRepository.save(user);

        return user;
    }

    private void checkIfExistsByEmail(UserRegistrationDto newUser) {
        if (usersRepository.existsByEmail(newUser.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT, "User with email <" + newUser.getEmail() + "> already exists");
        }
    }

    public UserDto getUserById(Long currentUserId) {
        return from(usersRepository.findById(currentUserId)
                                   .orElseThrow());
    }
}
