package de.allcom.dto.product;

import de.allcom.dto.auction.AuctionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductWithAuctionDto {
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

    @Schema(description = "imageLinks", example = "[\"/images/2/1ed74407-c18d-40e0-9b9b-c53b9fb99634.jpeg\"]")
    private List<String> imageLinks;

    @Schema(description = "Current auction of the product")
    private AuctionDto lastCreatedAuction;
}
