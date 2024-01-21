package de.allcom.controllers.auth;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.user.UserDto;
import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.services.auth.AuthentificationService;
import de.allcom.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173")
public class AuthentificationController {

    private final AuthentificationService authentificationService;
    @Operation(summary = "User Registration", description = "Available to everyone. Default role is Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409", description = "User with this email already exists", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<AuthentificationResponse> register(
            @RequestBody UserAddressRegistrationDto request) {
        return ResponseEntity.ok(authentificationService.register(request));
    }

    @Operation(summary = "Login and authenticate user", description = "Available to everyone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = AuthentificationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthentificationResponse> login(
            @RequestBody AuthentificationRequest request) {
        return ResponseEntity.ok(authentificationService.login(request));
    }

}
