package de.allcom.controllers.api;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.product.ProductWishlistDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tags(@Tag(name = "Wishlist"))
@RequestMapping("/api/wishlist")
public interface WishlistApi {
    @Operation(summary = "Products in users wishlist", description = "Default role is Client")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductWishlistDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    }
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    List<ProductWishlistDto> findProducts(@PathVariable Long userId);

    @Operation(summary = "Add product to wishlist", description = "Default role is Client")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductWishlistDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Product did not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("add/{userId}/{productId}")
    List<ProductWishlistDto> addProductToWishlist(@PathVariable @Min(value = 1,
            message = "User ID must be greater than or equal to 1") Long userId,
                                                  @PathVariable @Min(value = 1,
            message = "Product ID must be greater than or equal to 1") Long productId);

    @Operation(summary = "Remove product from wishlist", description = "Default role is Client")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth", scopes = {"client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product removed", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductWishlistDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Product did not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("remove/{userId}/{productId}")
    void removeProductFromWishlist(@PathVariable @Min(value = 1,
            message = "User ID must be greater than or equal to 1") Long userId,
                                   @PathVariable @Min(value = 1,
                                           message = "Product ID must be greater than or equal to 1") Long productId);
}
