package de.allcom.controllers.api;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.user.UserWithAddressRegistrationDto;
import de.allcom.dto.user.UserWithAddressResponseDto;
import de.allcom.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tags(@Tag(name = "Users"))
@RequestMapping("/api/users")
public interface UsersApi {
    String DEFAULT_LIMIT = "5";

    String DEFAULT_SKIP = "0";

    @Operation(summary = "Get all users (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"admin"})
    @GetMapping("/getAll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserWithAddressResponseDto.class)))),
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
    Page<UserWithAddressResponseDto> getAll(
            @Valid @RequestParam(name = "limit", defaultValue = DEFAULT_LIMIT) int limit,
            @Valid @RequestParam(name = "skip", defaultValue = DEFAULT_SKIP) int skip);

    @Operation(summary = "Update user (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"admin"})
    @PutMapping("/updateUser/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithAddressResponseDto.class))),
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
    UserWithAddressResponseDto updateUser(@Valid @RequestBody UserWithAddressRegistrationDto request,
                                          @PathVariable Long userId);

    @Operation(summary = "Get user profile")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN','STOREKEEPER')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"admin,user,storekeeper"})
    @GetMapping("/getUserProfile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithAddressResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })
    UserWithAddressResponseDto getUserProfile();

    @Operation(summary = "Found user (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"admin"})
    @GetMapping("/foundUserByEmail/{userEmail}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithAddressResponseDto.class))),
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
    UserWithAddressResponseDto foundUserByEmail(@PathVariable String userEmail);

    @Operation(summary = "Found user by ID (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"admin"})
    @GetMapping("/foundUserById/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithAddressResponseDto.class))),
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
    UserWithAddressResponseDto foundUserById(
            @PathVariable @Min(value = 1, message = "User ID must be greater than or equal to 1") Long userId);

    @Operation(summary = "Change status of user account (lock/unlock and ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"admin"})
    @PutMapping("/changeStatus/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status of User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithAddressResponseDto.class))),
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
    UserWithAddressResponseDto changeStatus(
            @PathVariable @Min(value = 1, message = "User ID must be greater than or equal to 1") Long userId,
            @RequestParam boolean isChecked,
            @RequestParam boolean isBlocked);

}
