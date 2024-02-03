package de.allcom.dto.storage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(name = "Storage Create Dto", description = "Data for add new place in storage")
public class StorageCreateDto {
    @NotBlank(message = "Area is required")
    @Size(min = 1, max = 1, message = "Area of the storage must be a single character")
    @Schema(description = "Area of storage", example = "R")
    private String area;

    @NotNull(message = "Rack is required")
    @Schema(description = "Rack of storage", example = "11")
    private Integer rack;

    @NotNull(message = "Section is required")
    @Schema(description = "Section of Rack", example = "3")
    private Integer section;

    @NotNull(message = "Shelf is required")
    @Schema(description = "Shelf of storage", example = "5")
    private Integer shelf;


}
