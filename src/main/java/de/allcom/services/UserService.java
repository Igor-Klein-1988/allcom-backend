package de.allcom.services;

import de.allcom.dto.user.UserAddressResponseDto;

import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.exceptions.NotFoundException;
import de.allcom.models.Address;
import de.allcom.models.User;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserAddressResponseDto> getAll() {
        List<Object[]> results = userRepository.findAllUsersWithAddresses();

        List<UserAddressResponseDto> dtos = new ArrayList<>();

        for (Object[] result : results) {
            User user = (User) result[0];
            Address address = (Address) result[1];
            UserAddressResponseDto dto = UserAddressResponseDto.from(user, address);
            dtos.add(dto);
        }

        return dtos;
    }

    public UserAddressResponseDto getUserProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = addressRepository.findByUser(user);
        return UserAddressResponseDto.from(user, address);
    }

    public UserAddressResponseDto updateUser(UserAddressRegistrationDto request, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found!"));
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        existingUser.setHashPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setCompanyName(request.getCompanyName());
        existingUser.setPosition(request.getPosition());
        existingUser.setTaxNumber(request.getTaxNumber());

        User savedUser = userRepository.save(existingUser);

        Optional<Address> optionalAddress = Optional.ofNullable(addressRepository.findByUser(savedUser));

        Address address = optionalAddress.orElseGet(Address::new);

        address.setPostIndex(request.getPostIndex());
        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        address.setHouseNumber(request.getHouseNumber());
        address.setUser(savedUser);

        addressRepository.save(address);

        return UserAddressResponseDto.from(savedUser, address);
    }

}
