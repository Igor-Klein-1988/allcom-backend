package de.allcom.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Product", description = "Product data")
public class ProductDto {
    @Schema(description = "Product identifier", example = "1")
    private Long id;

    @Schema(description = "Products name", example = "Smartphone")
    private String name;

    @Schema(description = "Description of products", example = "Good")
    private String description;

    @Schema(description = "Category id of product", example = "3")
    private Long categoryId;

    @Schema(description = "State of product", example = "3")
    private String state;

    @Schema(description = "Images", example = "[]")
    private List<String> photoLinks;
}
