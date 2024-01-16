package de.allcom.services;

import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.UserRepository;
import de.allcom.services.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressRepository addressRepository;

    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        //this.usersService = new UsersService(usersRepository, passwordEncoder);
        this.userService = new UserService(userRepository,addressRepository, passwordEncoder);
    }

    @Nested
    @DisplayName("register():")
    public class Register {

        @Test
        public void return_saved_new_user() {
            UserAddressRegistrationDto newUser = getNewUserRegistrationDto();
            when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);

//            UserDto actual = userService.register(newUser);
//            //verify(passwordEncoder).encode("qwerty007");
//            verify(userRepository).save(any());
//            UserDto expected = UserDto.builder()
//                                      .firstName(newUser.getFirstName())
//                                      .lastName(newUser.getLastName())
//                                      .email(newUser.getEmail())
//                                      .build();
//
//            assertEquals(expected, actual);
        }

        @Test
        public void throws_exception_for_exists_email() {
            UserAddressRegistrationDto newUser = getNewUserRegistrationDto();
            when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(true);

//            assertAll(() -> assertThrows(RestException.class, () -> userService.register(newUser)),
//                    () -> Mockito.verify(userRepository, Mockito.never()).save(Mockito.any()));
        }
    }

    private UserAddressRegistrationDto getNewUserRegistrationDto() {
        String newUserFirstName = "new First Name";
        String newUserLastName = "LastName";
        String newUserEmail = "user1@mail.com";
        return UserAddressRegistrationDto.builder()
                                  .firstName(newUserFirstName)
                                  .lastName(newUserLastName)
                                  .email(newUserEmail)
                                  .build();

    }
}
