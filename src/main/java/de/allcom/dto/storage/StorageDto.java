package de.allcom.dto.storage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(name = "Add a place to storage", description = "Data for add new place in storage")
public class StorageDto {

    @Schema(description = "id of storage", example = "1")
    private Long id;

    @NotBlank(message = "Area is required")
    @Size(min = 1, max = 1, message = "Area of the storage must be 1 big character")
    @Schema(description = "Area of storage", example = "R")
    private Character area;

    @NotBlank(message = "Rack is required")
    @Schema(description = "Rack of storage", example = "12")
    private Integer rack;

    @NotBlank(message = "Section is required")
    @Schema(description = "Section of storage", example = "12")
    private Integer section;

    @NotBlank(message = "Shelve is required")
    @Schema(description = "Shelve of storage", example = "12")
    private Integer shelve;
}
