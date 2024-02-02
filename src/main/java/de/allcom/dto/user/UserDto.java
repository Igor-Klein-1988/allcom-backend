package de.allcom.dto.user;

import de.allcom.models.Role;
import de.allcom.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "User", description = "User data")
public class UserDto {
    @Schema(description = "User identifier", example = "1")
    private Long id;

    @Schema(description = "User first name", example = "Alex")
    private String firstName;

    @Schema(description = "User last name", example = "Schmidt")
    private String lastName;

    @Schema(description = "User's email", example = "alex-schmidt@mail.com")
    private String email;

    @Schema(description = "User's role", example = "ADMIN")
    private Role role;

    @Schema(description = "Status indicating whether the user is checked", example = "false")
    private boolean isChecked;

    @Schema(description = "Status indicating whether the user is blocked", example = "false")
    private boolean isBlocked;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .isChecked(user.isChecked())
                .isBlocked(user.isBlocked())
                .build();
    }
}
