package de.allcom.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Category", description = "Categories of products in the language")
public class CategoryByLanguageDto {

    @Schema(description = "Category identifier", example = "1")
    private Long id;

    @Schema(description = "Category name english", example = "Iron")
    private String name;

    private Long parentId;
}
