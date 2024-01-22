package de.allcom.services.utils;

import de.allcom.dto.product.ProductDto;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Builder
public class Converters {
    public ProductDto fromProductToProductDto(Product product) {
        List<String> altPhotos = (product.getImages() != null) ? product.getImages().stream()
                .map(ProductImage::getLink).toList() : Collections.emptyList();
        return new ProductDto(product.getId(),
                product.getName(), product.getDescription(), product.getCategory().getId(),
                altPhotos);
    }
}
