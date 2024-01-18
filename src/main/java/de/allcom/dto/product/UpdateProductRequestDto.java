package de.allcom.dto.product;

import de.allcom.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(name = "Update product", description = "Data for update product")
public class UpdateProductRequestDto {

    @Schema(description = "Id of the product", example = "1")
    private Long id;

    @NotBlank(message = "Name")
    @Size(min = 3, max = 50, message = "Name of the product must be between 3 and 50 characters")
    @Schema(description = "Name of the product", example = "Regal")
    private String name;

    @NotBlank(message = "Description")
    @Schema(description = "Description of product", example = "Sehr gute Qualitet")
    private String description;

    @Schema(description = "Weight of the product/kg", example = "3.5")
    private Float weight;

    @Schema(description = "Color of the product/kg", example = "Weiß")
    private String color;

    @Schema(description = "Category", example = "fsda")
    private Long categoryId;

    @Schema(description = "Photos of the product, jpeg", example = "foto1.jpeg")
    private List<MultipartFile> photos;
}
