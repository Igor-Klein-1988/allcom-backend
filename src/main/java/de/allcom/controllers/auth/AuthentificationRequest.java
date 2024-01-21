package de.allcom.controllers.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthentificationRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email address must be in a valid format (e.g., user@example.com)")
    @Schema(description = "User's email", example = "james-smith@mail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, numbers, and special characters")
    @Schema(description = "User's password", example = "Qwerty007!")

    private String password;
}
