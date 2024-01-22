package de.allcom.controllers;

import de.allcom.controllers.api.ProductApi;
import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.CreateProductRequestDto;
import de.allcom.dto.product.UpdateProductRequestDto;
import de.allcom.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {
    private final ProductService productService;

    @Override
    public Page<ProductDto> getAllProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productService.getAllProducts(pageRequest);
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(UpdateProductRequestDto request, Long productId) {
        ProductDto updatedProduct = productService.updateProduct(request, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDto> createProduct(CreateProductRequestDto request) {
        ProductDto createdProduct = productService.createProductWithPhotos(request);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
}
