package de.allcom.services.utils;

import de.allcom.dto.product.ProductDto;
import de.allcom.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Builder
public class Converters {
    public ProductDto fromProductToProductDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getCategoryName(), product.getLinksOfPhotos());
    }
}
