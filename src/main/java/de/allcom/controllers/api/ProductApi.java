package de.allcom.controllers.api;

import de.allcom.dto.product.CreateProductRequestDto;
import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.UpdateProductRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tags(@Tag(name = "Products"))
@RequestMapping("/api/products")
public interface ProductApi {
    @Operation(summary = "Product add", description = "Default role is Admin")

    @PostMapping("/add")
    ResponseEntity<ProductDto> createProduct(@ModelAttribute @Valid CreateProductRequestDto request);

    @GetMapping
    Page<ProductDto> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @PutMapping("/{productId}")
    ResponseEntity<ProductDto> updateProduct(@ModelAttribute @Valid UpdateProductRequestDto request, @PathVariable Long productId);

}
