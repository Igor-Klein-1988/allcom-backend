package de.allcom.dto.product;

import de.allcom.dto.auction.AuctionDto;
import de.allcom.dto.storage.StorageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponseDto {
    @Schema(description = "Product identifier", example = "1")
    private Long id;

    @Schema(description = "Products name", example = "Smartphone")
    private String name;

    @Schema(description = "Description of products", example = "Good")
    private String description;

    @Schema(description = "Weight of product", example = "2.5")
    private Float weight;

    @Schema(description = "Color of product", example = "Black")
    private String color;

    @Schema(description = "Category id of product", example = "3")
    private Long categoryId;

    @Schema(description = "State of product", example = "IN_STOCK")
    private String state;

    @Schema(description = "imageLinks", example = "[]")
    private List<String> imageLinks;

    @Schema(description = "Current auction of the product")
    private AuctionDto lastCreatedAuction;

    @Schema(description = "Storage of the product")
    private StorageDto storage;
}
