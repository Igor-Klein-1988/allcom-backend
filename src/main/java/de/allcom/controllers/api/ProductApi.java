package de.allcom.controllers.api;

import de.allcom.dto.forms.ProductResponseValues;
import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.SaveProductRequestDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tags(@Tag(name = "Products"))
@RequestMapping("/api/products")
public interface ProductApi {
    @Operation(summary = "Product add", description = "Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "404", description = "Product did not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    ProductResponseValues saveProduct(@ModelAttribute @Valid SaveProductRequestDto request);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    Page<ProductResponseValues> getAllProducts(
            @RequestParam int page,
            @RequestParam int size);


    @GetMapping("/product/{id}")
    ProductResponseValues findById(@PathVariable Long id);

}
