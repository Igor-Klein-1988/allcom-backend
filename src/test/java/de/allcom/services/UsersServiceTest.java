package de.allcom.services;

import de.allcom.dto.user.UserDto;
import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.exceptions.RestException;
import de.allcom.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class UsersServiceTest {
    @Mock
    private UsersRepository usersRepository;

    //@Mock
    //private PasswordEncoder passwordEncoder;

    private UsersService usersService;

    @BeforeEach
    void setUp() {
        //this.usersService = new UsersService(usersRepository, passwordEncoder);
        this.usersService = new UsersService(usersRepository);
    }

    @Nested
    @DisplayName("register():")
    public class Register {

        @Test
        public void return_saved_new_user() {
            UserAddressRegistrationDto newUser = getNewUserRegistrationDto();
            when(usersRepository.existsByEmail(newUser.getEmail())).thenReturn(false);

            UserDto actual = usersService.register(newUser);
            //verify(passwordEncoder).encode("qwerty007");
            verify(usersRepository).save(any());
            UserDto expected = UserDto.builder()
                                      .firstName(newUser.getFirstName())
                                      .lastName(newUser.getLastName())
                                      .email(newUser.getEmail())
                                      .build();

            assertEquals(expected, actual);
        }

        @Test
        public void throws_exception_for_exists_email() {
            UserAddressRegistrationDto newUser = getNewUserRegistrationDto();
            when(usersRepository.existsByEmail(newUser.getEmail())).thenReturn(true);

            assertAll(() -> assertThrows(RestException.class, () -> usersService.register(newUser)),
                    () -> Mockito.verify(usersRepository, Mockito.never()).save(Mockito.any()));
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
