package de.allcom.dto.product;

import de.allcom.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AddProduct", description = "Data for add new product")
public class CreateProductRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name of the product must be between 3 and 50 characters")
    @Schema(description = "Name of the product", example = "Regal")
    private String name;

    @NotBlank(message = "Description is required")
    @Schema(description = "Description of product", example = "Sehr gute Qualitet")
    private String description;

    @Schema(description = "Weight of the product/kg", example = "3.5")
    private Float weight;

    @Schema(description = "Color of the product/kg", example = "Weiß")
    private String color;

    @Schema(description = "Category", example = "fsda")
    private Category category;

    @Schema(description = "Photos of the product, jpeg", example = "foto1.jpeg")
    private List<MultipartFile> photos;
}
