package de.allcom.controllers.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AuthentificationResponse", description = "Authentification response")
public class AuthentificationResponse {

    @Schema(description = "User id", example = "1")
    private Long id;

    @Schema(description = "User first name", example = "Alex")
    private String firstName;

    @Schema(description = "User last name", example = "Schmidt")
    private String lastName;

    @Schema(description = "User's email", example = "alex-schmidt@mail.com")
    private String email;

    @Schema(description = "User token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0I")
    private String token;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuthentificationResponse that = (AuthentificationResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(email, that.email)
                && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, token);
    }
}
