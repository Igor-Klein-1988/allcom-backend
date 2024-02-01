package de.allcom.controllers;

import de.allcom.controllers.api.ProductApi;
import de.allcom.dto.forms.ProductResponseValues;
import de.allcom.dto.product.SaveProductRequestDto;
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
    public ProductResponseValues saveProduct(SaveProductRequestDto productRequestDto) {
        return productService.saveProduct(productRequestDto);
    }

    @Override
    public ProductResponseValues findById(Long id) {
        return productService.findById(id);
    }

    @Override
    public Page<ProductResponseValues> searchByCategoryOrName(Long id, String searchQuery,
                                                             int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productService.searchByCategoryOrName(id, searchQuery, pageRequest);
    }
}
