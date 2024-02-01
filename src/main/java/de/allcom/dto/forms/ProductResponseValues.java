package de.allcom.dto.forms;

import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.StorageDto;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponseValues {
    private ProductDto product;
    //    private AuctionDTO auction;
    private StorageDto storage;

    public static ProductResponseValues from(Product product) {
        return ProductResponseValues.builder()
                .product(ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .weight(product.getWeight())
                        .color(product.getColor())
                        .categoryId(product.getCategory().getId())
                        .state(product.getState().name())
                        .photoLinks(product.getImages().stream().map(ProductImage::getLink).toList())
                        .build())
                .storage(StorageDto.builder()
                        .id(product.getStorage().getId())
                        .areaName(product.getStorage().getArea().getName().name())
                        .rackNumber(product.getStorage().getRack().getNumber())
                        .sectionNumber(product.getStorage().getSection().getNumber())
                        .shelveNumber(product.getStorage().getShelve().getNumber())
                        .build())
                .build();
    }
}
