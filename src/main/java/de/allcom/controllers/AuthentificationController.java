package de.allcom.controllers;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.auth.AuthentificationRequestDto;
import de.allcom.dto.auth.AuthentificationResponseDto;
import de.allcom.dto.auth.ChangePasswordRequestDto;
import de.allcom.dto.user.UserDto;
import de.allcom.dto.user.UserWithAddressRegistrationDto;
import de.allcom.services.auth.AuthentificationService;
import de.allcom.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthentificationController {

    private final AuthentificationService authentificationService;

    @Operation(summary = "User Registration", description = "Available to everyone. Default role is Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "User with this email already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public AuthentificationResponseDto register(
            @Valid @RequestBody UserWithAddressRegistrationDto request) {
        return authentificationService.register(request);
    }

    @Operation(summary = "Login and authenticate user", description = "Available to everyone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthentificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PostMapping("/login")
    public AuthentificationResponseDto login(
            @Valid @RequestBody AuthentificationRequestDto request) {
        return authentificationService.login(request);
    }

    @Operation(summary = "Change user password", description = "Available to everyone.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN','STOREKEEPER')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"admin,user,storekeeper"})
    @PostMapping("/changePassword")
    public StandardResponseDto changePassword(
            @Valid @RequestBody ChangePasswordRequestDto request,
            Principal connectedUser) {
        return authentificationService.changePassword(request, connectedUser);
    }
}
