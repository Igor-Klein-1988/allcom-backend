package de.allcom.controllers.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    @Pattern(regexp = "^(?=.*[A-Za-zßäöüÄÖÜ])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include letters, numbers,"
                    + " and special characters")
    @Schema(description = "Current user password", example = "Qwerty007!")
    private String currentPassword;

    @Pattern(regexp = "^(?=.*[A-Za-zßäöüÄÖÜ])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include letters, numbers,"
                    + " and special characters")
    @Schema(description = "New user password", example = "qweRTY007!")
    private String newPassword;

    @Pattern(regexp = "^(?=.*[A-Za-zßäöüÄÖÜ])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include letters, numbers,"
                    + " and special characters")
    @Schema(description = "New user confirmation password", example = "qweRTY007!")
    private String confirmationPassword;
}
