package de.allcom.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Storage", description = "Storage data")
public class StorageDto {
    @Schema(description = "Storage identifier", example = "1")
    private Long id;

    @Schema(description = "Storage area name", example = "L")
    private String areaName;

    @Schema(description = "Storage rack", example = "1")
    private Integer rackNumber;

    @Schema(description = "Storage section", example = "1")
    private Integer sectionNumber;

    @Schema(description = "Storage shelve", example = "1")
    private Integer shelveNumber;
}
