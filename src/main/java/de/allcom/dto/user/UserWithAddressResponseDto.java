package de.allcom.dto.user;

import de.allcom.models.Address;
import de.allcom.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserWithAddressResponseDto {

    @Schema(description = "User identifier", example = "1")
    private Long id;
    @Schema(description = "User first name", example = "Alex")
    private String firstName;
    @Schema(description = "User last name", example = "Schmidt")
    private String lastName;
    @Schema(description = "User's email", example = "alex-schmidt-new-mail@mail.com")
    private String email;
    @Schema(description = "User's phone number", example = "490123456789")
    private String phoneNumber;
    @Schema(description = "Company name", example = "Allcom GmbH & Co. KG")
    private String companyName;
    @Schema(description = "User's position", example = "Manager")
    private String position;
    @Schema(description = "Company's tax's number", example = "123456789")
    private String taxNumber;
    @Schema(description = "Address")
    private AddressDto address;
    @Schema(description = "User's role", example = "CLIENT")
    private String role;
    @Schema(description = "User checked status", example = "false")
    private boolean isChecked;
    @Schema(description = "User blocked status", example = "false")
    private boolean isBlocked;

    public static UserWithAddressResponseDto from(User user, Address address) {
        UserWithAddressResponseDto dto = new UserWithAddressResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCompanyName(user.getCompanyName());
        dto.setPosition(user.getPosition());
        dto.setTaxNumber(user.getTaxNumber());

        AddressDto addressDto = new AddressDto();
        addressDto.setPostIndex(address.getPostIndex());
        addressDto.setCity(address.getCity());
        addressDto.setStreet(address.getStreet());
        addressDto.setHouseNumber(address.getHouseNumber());

        dto.setAddress(addressDto);

        dto.setRole(user.getRole().name());
        dto.setChecked(user.isChecked());
        dto.setBlocked(user.isBlocked());

        return dto;
    }
}
