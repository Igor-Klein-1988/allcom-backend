package de.allcom.dto.storage;

import de.allcom.models.Storage;
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
@Schema(name = "Add a place to storage", description = "Data for add new place in storage")
public class StorageDto {
    @NotNull(message = "Storage identifier is required")
    @Schema(description = "Storage identifier", example = "1")
    private Long id;

    @NotBlank(message = "Area is required")
    @Size(min = 1, max = 1, message = "Area of the storage must be a single character")
    @Schema(description = "Area of storage", example = "R")
    private String area;

    @NotNull(message = "Rack is required")
    @Schema(description = "Rack of storage", example = "12")
    private Integer rack;

    @NotNull(message = "Section is required")
    @Schema(description = "Section of storage", example = "12")
    private Integer section;

    @NotNull(message = "shelf is required")
    @Schema(description = "shelf of storage", example = "12")
    private Integer shelf;

    @Schema(description = "Product identifier", example = "2")
    private Long productId;

    public static StorageDto from(Storage storage) {

        return StorageDto.builder()
                         .id(storage.getId())
                         .area(storage.getArea().getName().toString())
                         .rack(storage.getRack().getNumber())
                         .section(storage.getSection().getNumber())
                         .shelf(storage.getShelf().getNumber())
                         .productId(storage.getProduct().getId())
                         .build();
    }
}
