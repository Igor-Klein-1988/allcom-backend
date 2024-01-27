package de.allcom.controllers;

import de.allcom.controllers.api.ProductApi;
import de.allcom.dto.product.CreateProductRequestDto;
import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.UpdateProductRequestDto;
import de.allcom.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ProductDto updateProduct(UpdateProductRequestDto request, Long productId) {
        return productService.updateProduct(request, productId);
    }

    @Override
    public ProductDto findById(Long id) {
        return productService.findById(id);
    }

    @Override
    public ProductDto createProduct(CreateProductRequestDto request) {
        return productService.createProductWithPhotos(request);
    }
}
