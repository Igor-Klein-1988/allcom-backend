package de.allcom.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Schema(name = "UserRegistration", description = "Registration data")
public class UserWithAddressRegistrationDto {
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Schema(description = "User first name", example = "Alex")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Schema(description = "User last name", example = "Schmidt")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include letters, numbers,"
            + " and special characters")
    @Schema(description = "User's password", example = "Qwerty007!")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email address must be in a valid format (e.g., user@example.com)")
    @Schema(description = "User's email", example = "alex-schmidt@mail.com")
    private String email;

    @Schema(description = "User's phone number", example = "+491753456755")
    @Size(max = 13, message = "Phone number should not exceed 12 characters")
    private String phoneNumber;

    @Schema(description = "Company name", example = "Allcom GmbH")
    private String companyName;

    @Schema(description = "User's position", example = "Purchasing manager")
    private String position;

    @Schema(description = "Company's tax's number", example = "3458795653")
    @Size(max = 13, message = "Tax number should not exceed 13 characters")
    private String taxNumber;

    @Schema(description = "Adddress")
    @Valid
    private AddressDto address;

    @Schema(description = "Status indicating whether the user is checked", example = "false")
    private boolean isChecked;

    @Schema(description = "Status indicating whether the user is blocked", example = "false")
    private boolean isBlocked;
}
