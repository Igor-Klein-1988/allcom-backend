package de.allcom.controllers.api;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.product.ProductCreateRequestDto;
import de.allcom.dto.product.ProductResponseDto;
import de.allcom.dto.product.ProductUpdateRequestDto;
import de.allcom.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tags(@Tag(name = "Products"))
@RequestMapping("/api/products")
public interface ProductApi {
    String DEFAULT_PAGE = "0";
    String DEFAULT_SIZE = "20";

    @Operation(summary = "Add Product", description = "Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "404", description = "Product did not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "422", description = "Failed to save file", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    ProductResponseDto create(@RequestParam("auction") String auctionJson, @RequestParam("storage") String storageJson,
            @ModelAttribute ProductCreateRequestDto productDto);

    @Operation(summary = "Update Product", description = "Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product updated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "404", description = "Product did not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "422", description = "Failed to save file", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))})

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/update")
    ProductResponseDto update(@RequestParam("auction") String auctionJson, @RequestParam("storage") String storageJson,
            @ModelAttribute ProductUpdateRequestDto productDto);

    @Operation(summary = "Find Product By productId", description = "Default role is Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request was processed successfully", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Product did not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))})
    @GetMapping("/{productId}")
    ProductResponseDto findById(@PathVariable(name = "productId")
                                @Parameter(description = "Product identifier", example = "1") Long productId);

    @Operation(summary = "Find Products by categoryID and/or productName", description = "Default role is Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request was processed successfully", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))})
    @GetMapping("/search")
    Page<ProductResponseDto> searchByCategoryOrName(@RequestParam(name = "categoryId", required = false)
            Long categoryId, @RequestParam(required = false) String searchQuery,
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = "size", defaultValue = DEFAULT_SIZE) int size);
}
