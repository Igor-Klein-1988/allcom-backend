package de.allcom.controllers.auth;

import de.allcom.dto.user.UserAddressRegistrationDto;
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
        // Создаем тестовые данные
        UserAddressRegistrationDto request = new UserAddressRegistrationDto();
        AuthentificationResponse expectedResponse = new AuthentificationResponse();
        expectedResponse.setToken("mockedToken");

        // Задаем поведение сервиса при вызове метода register
        when(authentificationService.register(any(UserAddressRegistrationDto.class)))
                .thenReturn(expectedResponse);

        // Вызываем метод контроллера
        AuthentificationResponse response = authentificationController.register(request);

        // Проверяем, что ответ соответствует ожиданиям
        assertEquals(expectedResponse, response);
    }

    @Test
    void testLogin() {
        // Создаем тестовые данные
        AuthentificationRequest authRequest = new AuthentificationRequest();
        AuthentificationResponse expectedResponse = new AuthentificationResponse();
        expectedResponse.setToken("mockedToken");

        // Задаем поведение сервиса при вызове метода login
        when(authentificationService.login(any(AuthentificationRequest.class)))
                .thenReturn(expectedResponse);

        // Вызываем метод контроллера
        AuthentificationResponse response = authentificationController.login(authRequest);

        // Проверяем, что ответ соответствует ожиданиям
        assertEquals(expectedResponse, response);
    }
}