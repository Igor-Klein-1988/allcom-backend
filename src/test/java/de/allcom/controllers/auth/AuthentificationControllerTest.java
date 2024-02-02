package de.allcom.controllers.auth;

import de.allcom.controllers.AuthentificationController;
import de.allcom.dto.auth.AuthentificationRequestDto;
import de.allcom.dto.auth.AuthentificationResponseDto;
import de.allcom.dto.user.UserWithAddressRegistrationDto;
import de.allcom.services.auth.AuthentificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthentificationControllerTest {

    @Mock
    private AuthentificationService authentificationService;

    @InjectMocks
    private AuthentificationController authentificationController;

    @Test
    void testRegister() {
        UserWithAddressRegistrationDto request = new UserWithAddressRegistrationDto();
        AuthentificationResponseDto expectedResponse = new AuthentificationResponseDto();
        expectedResponse.setToken("mockedToken");

        when(authentificationService.register(any(UserWithAddressRegistrationDto.class)))
                .thenReturn(expectedResponse);

        AuthentificationResponseDto response = authentificationController.register(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testLogin() {
        AuthentificationRequestDto authRequest = new AuthentificationRequestDto();
        AuthentificationResponseDto expectedResponse = new AuthentificationResponseDto();
        expectedResponse.setToken("mockedToken");

        when(authentificationService.login(any(AuthentificationRequestDto.class)))
                .thenReturn(expectedResponse);

        AuthentificationResponseDto response = authentificationController.login(authRequest);

        assertEquals(expectedResponse, response);
    }
}