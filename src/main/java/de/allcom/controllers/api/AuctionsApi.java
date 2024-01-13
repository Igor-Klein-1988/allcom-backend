package de.allcom.controllers.api;

import de.allcom.dto.AuctionRequestDto;
import de.allcom.dto.AuctionResponseDto;
import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.user.UserDto;
import de.allcom.dto.user.UserRegistrationDto;
import de.allcom.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tags(@Tag(name = "Auctions"))
@RequestMapping("/api/auctions")
public interface AuctionsApi {
    @PostMapping("")
    AuctionResponseDto create(@RequestBody @Valid AuctionRequestDto request);
//    @Operation(summary = "User Registration", description = "Available to everyone. Default role is Client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "User registered", content =
//                @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
//            @ApiResponse(responseCode = "400", description = "Validation error", content =
//                @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class))),
//            @ApiResponse(responseCode = "409", description = "User with this email already exists", content =
//                @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
//    )
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/register")
//    UserDto register(@RequestBody @Valid UserRegistrationDto request);
}
