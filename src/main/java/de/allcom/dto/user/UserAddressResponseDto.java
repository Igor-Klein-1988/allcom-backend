package de.allcom.dto.user;

import lombok.Data;

@Data
public class UserAddressResponseDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String companyName;
    private String position;
    private String inn;
    private String index;
    private String city;
    private String street;
    private String houseNumber;
}
