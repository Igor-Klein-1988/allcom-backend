package de.allcom.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UserRegistration", description = "Registration data")
public class UserRegistrationDto {
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Schema(description = "User first name", example = "Alex")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Schema(description = "User last name", example = "Schmidt")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include letters, numbers,"
                    + " and special characters")
    @Schema(description = "User's password", example = "Qwerty007!")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email address must be in a valid format (e.g., user@example.com)")
    @Schema(description = "User's email", example = "alex-schmidt@mail.com")
    private String email;
}
