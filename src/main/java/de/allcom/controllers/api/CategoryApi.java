package de.allcom.controllers.api;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.category.CategoryByLanguageDto;
import de.allcom.dto.category.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Categories with All Names", description = "Available to everyone. Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request was processed successfully", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Your request of categories is wrong", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Categories does not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    List<CategoryDto> findCategoriesWithAllNames();

    @Operation(summary = "Categories with one language", description = "Available to everyone. Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request was processed successfully", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Your request of categories is wrong", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Categories does not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{language}")
    List<CategoryByLanguageDto> findCategoriesByLanguage(
            @PathVariable @Valid @Size(min = QUANTITY_OF_DIGITS_FOR_LANGUAGE, max = QUANTITY_OF_DIGITS_FOR_LANGUAGE)
            @Parameter(description = "Language identifier", example = "ru") String language);

    @Operation(summary = "Get Category by id", description = "Available to everyone. Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request was processed successfully", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Your request of categories is wrong", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Categories does not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/one/{id}")
    CategoryDto findCategoryById(@PathVariable @Parameter(description = "Category identifier", example = "1")  Long id);

    @Operation(summary = "Get Category by id with one language",
            description = "Available to everyone. Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request was processed successfully", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Your request of categories is wrong", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Categories does not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{categoryId}/{language}")
    CategoryByLanguageDto findCategoryByLanguage(
            @PathVariable(name = "categoryId") @Parameter(description = "Language Category", example = "1")
            Long categoryId, @PathVariable @Valid @Size(min = QUANTITY_OF_DIGITS_FOR_LANGUAGE,
            max = QUANTITY_OF_DIGITS_FOR_LANGUAGE)
            @Parameter(description = "Language identifier", example = "ru") String language);

    @Operation(summary = "Get Categories by parentId", description = "Available to everyone. Default role is Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request was processed successfully", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Your request of categories is wrong", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Categories does not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponseDto.class)))}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/allByParent/{parentId}")
    List<CategoryDto> findCategoriesByParentId(@PathVariable(name = "parentId")
                                @Parameter(description = "Parent Category identifier", example = "78") Long parentId);
}
