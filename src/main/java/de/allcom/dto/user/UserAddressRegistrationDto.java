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
public class UserAddressRegistrationDto {
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 50 characters")
    @Schema(description = "User first name", example = "Alex")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 50 characters")
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

    @Schema(description = "User's phone number", example = "0123456789")
    private String phoneNumber;

    @Schema(description = "Company name", example = "Allcom GmbH")
    private String companyName;

    @Schema(description = "User's position", example = "Manager")
    private String position;

    @Schema(description = "Company's tax's number", example = "123456789")
    private String taxNumber;

    @Schema(description = "Company's index", example = "10176")
    private String postIndex;

    @Schema(description = "Company's city", example = "Berlin")
    private String city;

    @Schema(description = "Company's street", example = "Alexanderplatz")
    private String street;

    @Schema(description = "Company's house number", example = "1")
    private String houseNumber;

    @Schema(description = "User blocked status", example = "false")
    private boolean isBlocked;
}
