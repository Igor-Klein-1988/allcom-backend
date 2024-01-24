package de.allcom.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "Update product", description = "Data for update product")
public class UpdateProductRequestDto {

    @Schema(description = "Id of the product", example = "1")
    private Long id;

    @NotBlank(message = "Name")
    @Size(min = 3, max = 50, message = "Name of the product must be between 3 and 50 characters")
    @Schema(description = "Name of the product", example = "TV")
    private String name;

    @NotBlank(message = "Description")
    @Schema(description = "Description of product", example = "Good quality")
    private String description;

    @Schema(description = "Weight of the product/kg", example = "3.5")
    private Float weight;

    @Schema(description = "Color of the product", example = "black")
    @Size(min = 3, max = 10, message = "Color of the product must be between 3 and 50 characters")
    private String color;

    @Schema(description = "Category id", example = "1")
    private Long categoryId;

    @Schema(description = "Photos of the product, jpeg", example = "photo1.jpeg")
    private List<MultipartFile> images;

    @Schema(description = "Links of products photos", example = "/path/path1/photo1.jpeg")
    private List<String> imageLinks;

}
