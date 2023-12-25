package de.allcom.validation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "ValidationErrors", description = "информация об ошибках валидации")
public class ValidationErrorsDto {

    @Schema(description = "список ошибок валидации")
    private List<ValidationErrorDto> errors;
}
