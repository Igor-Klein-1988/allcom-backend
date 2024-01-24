package de.allcom.services.utils;

import de.allcom.dto.product.ProductDto;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Builder
public class Converters {
    public ProductDto fromProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .photoLinks(product.getImages().stream().map(ProductImage::getLink).toList())
                .build();
    }
}
