package de.allcom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "User", description = "User data")
public class UserDto {
    @Schema(description = "User identifier", example = "1")
    private Integer id;

    @Schema(description = "Username of the user", example = "Igor123")
    private String username;

    @Schema(description = "Role of the user", example = "User")
    private String role;

    @Schema(description = "User's email", example = "user@mail.com")
    private String email;
}
