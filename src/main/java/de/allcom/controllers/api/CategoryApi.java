package de.allcom.controllers.api;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.category.CategoryDto;
import de.allcom.dto.category.CategoryLanguageDto;
import de.allcom.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tags(@Tag(name = "Categories"))
@RequestMapping("/api/categories")
public interface CategoryApi {
    int QUANTITY_OF_DIGITS_FOR_LANGUAGE = 2;

    @Operation(summary = "Categories", description = "Available to everyone. Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "404", description = "Categories is not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "409", description = "User with this email already exists", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    List<CategoryDto> findAllCategoriesWithAllNames();

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{language}")
    List<CategoryLanguageDto> findAllCategoryWithLanguage(
            @PathVariable @Valid @Size(min = QUANTITY_OF_DIGITS_FOR_LANGUAGE,
                    max = QUANTITY_OF_DIGITS_FOR_LANGUAGE) String language);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/one/{id}")
    CategoryDto findOneCategoryWithAllField(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/{language}")
    CategoryLanguageDto findOneCategoryWithLanguage(
            @PathVariable Long id,
            @PathVariable @Valid @Size(min = QUANTITY_OF_DIGITS_FOR_LANGUAGE,
                    max = QUANTITY_OF_DIGITS_FOR_LANGUAGE) String language);
}
