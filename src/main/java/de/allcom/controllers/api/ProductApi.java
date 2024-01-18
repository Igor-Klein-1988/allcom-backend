package de.allcom.controllers.api;

import de.allcom.dto.product.CreateProductRequestDto;
import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.UpdateProductRequestDto;
import de.allcom.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tags(@Tag(name = "Products"))
@RequestMapping("/api/products")
public interface ProductApi {
    @Operation(summary = "Product add", description = "Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class)))}
    )
    @PostMapping("/add")
    ResponseEntity<ProductDto> createProduct(@ModelAttribute @Valid CreateProductRequestDto request);

    @GetMapping
    Page<ProductDto> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @PutMapping("/{productId}")
    ResponseEntity<ProductDto> updateProduct(@ModelAttribute @Valid UpdateProductRequestDto request);

}
