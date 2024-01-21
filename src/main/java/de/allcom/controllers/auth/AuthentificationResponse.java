package de.allcom.controllers.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthentificationResponse {

    @Schema(description = "User id", example = "1")
    private Long id;

    @Schema(description = "User first name", example = "Alex")
    private String firstName;

    @Schema(description = "User last name", example = "Schmidt")
    private String lastName;

    @Schema(description = "User's email", example = "alex-schmidt@mail.com")
    private String email;

    @Schema(description = "User token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String token;
}
