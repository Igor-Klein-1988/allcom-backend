package de.allcom.dto.user;

import de.allcom.models.Address;
import de.allcom.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserAddressResponseDto {

    private Long userId;
    @Schema(description = "User first name", example = "Alex")
    private String firstName;
    @Schema(description = "User last name", example = "Schmidt")
    private String lastName;
    @Schema(description = "User's email", example = "alex-schmidt-new-mail@mail.com")
    private String email;
    @Schema(description = "User's phone number", example = "+490123456789")
    private String phoneNumber;
    @Schema(description = "Company name", example = "Allcom GmbH & Co. KG")
    private String companyName;
    @Schema(description = "User's position", example = "Manager")
    private String position;
    @Schema(description = "Company's tax's number", example = "123456789")
    private String taxNumber;
    @Schema(description = "Company's index", example = "10176")
    private String index;
    @Schema(description = "Company's city", example = "Berlin")
    private String city;
    @Schema(description = "Company's street", example = "Alexanderplatz")
    private String street;
    @Schema(description = "Company's house number", example = "1")
    private String houseNumber;

    public static UserAddressResponseDto from(User user, Address address) {
        UserAddressResponseDto dto = new UserAddressResponseDto();
        dto.setUserId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCompanyName(user.getCompanyName());
        dto.setPosition(user.getPosition());
        dto.setTaxNumber(user.getTaxNumber());
        dto.setIndex(address.getPostIndex());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setHouseNumber(address.getHouseNumber());
        return dto;
    }
}
