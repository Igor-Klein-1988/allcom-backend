package de.allcom.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Address", description = "User address")
public class AddressDto {

    @Schema(description = "Company's index", example = "10176")
    @Size(max = 5, message = "Index should not exceed 5 characters")
    @NotBlank(message = "Post index is required")
    private String postIndex;

    @Schema(description = "Company's city", example = "Berlin")
    @NotBlank(message = "City is required")
    private String city;

    @Schema(description = "Company's street", example = "Alexanderplatz")
    @NotBlank(message = "Street is required")
    private String street;

    @Schema(description = "Company's house number", example = "1")
    @NotBlank(message = "House number is required")
    private String houseNumber;
}
