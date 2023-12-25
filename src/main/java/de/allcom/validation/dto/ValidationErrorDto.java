package de.allcom.validation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "ValidationError", description = "Description of validation error")
public class ValidationErrorDto {
    @Schema(description = "Name of the field where the error occurred", example = "email")
    private String field;
    @Schema(description = "Value entered by the user that was rejected by the server", example = "myemail.com")
    private String rejectedValue;
    @Schema(description = "Message to be shown to the user", example = "Email address must be in a valid format "
            + "(e.g., user@example.com)")
    private String message;
}
