package de.allcom.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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

    @Schema(description = "Category name of product", example = "Phone")//id_category
    private String categoryName;

    @Schema(description = "Images", example = "[]")//photos переименовать
    private List<String> photoLinks;

}
