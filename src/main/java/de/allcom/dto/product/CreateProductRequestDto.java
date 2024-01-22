package de.allcom.dto.product;

import de.allcom.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "Add Product", description = "Data for add new product")
public class CreateProductRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name of the product must be between 3 and 50 characters")
    @Schema(description = "Name of the product", example = "TV")
    private String name;

    @NotBlank(message = "Description is required")
    @Schema(description = "Description of product", example = "Good quality")
    private String description;

    @Schema(description = "Weight of the product/kg", example = "3.5")
    private Float weight;

    @Schema(description = "Color of the product", example = "Black")
    @Size(min = 3, max = 10, message = "Color of the product must be between 3 and 50 characters")
    private String color;

    @Schema(description = "Category", example = "TV")
    private Category category;

    @Schema(description = "Photos of the product, jpeg", example = "foto1.jpeg")
    private List<MultipartFile> images;
}
