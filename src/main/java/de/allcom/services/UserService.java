package de.allcom.services;

import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.dto.user.UserAddressResponseDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Address;
import de.allcom.models.User;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserAddressResponseDto> getAll(PageRequest pageRequest) {
        Page<Object[]> results = userRepository.findAllUsersWithAddresses(pageRequest);
        return results.map(result -> UserAddressResponseDto.from((User) result[0], (Address) result[1]));
    }

    public UserAddressResponseDto getUserProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = addressRepository.findByUser(user);
        return UserAddressResponseDto.from(user, address);
    }

    public UserAddressResponseDto updateUser(UserAddressRegistrationDto request, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found!"));
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        existingUser.setHashPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setCompanyName(request.getCompanyName());
        existingUser.setPosition(request.getPosition());
        existingUser.setTaxNumber(request.getTaxNumber());
        existingUser.setChecked(request.isChecked());
        existingUser.setBlocked(request.isBlocked());
        existingUser.setUpdateAt(LocalDateTime.now());

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

    public UserAddressResponseDto foundUserByEmail(String userEmail) {
        User user = (User) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "User with email " + userEmail + " not found!"));
        Address address = addressRepository.findByUser(user);
        return UserAddressResponseDto.from(user, address);
    }

    public UserAddressResponseDto foundUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " not found!"));
        Address address = addressRepository.findByUser(user);
        return UserAddressResponseDto.from(user, address);
    }

    public UserAddressResponseDto changeStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found!"));
        user.setBlocked(Boolean.parseBoolean(status));
        userRepository.save(user);
        return UserAddressResponseDto.from(user, addressRepository.findByUser(user));
    }
}
