package de.allcom.services;

import de.allcom.dto.user.AddressDto;
import de.allcom.dto.user.UserWithAddressResponseDto;
import de.allcom.dto.user.UserWithAddressUpdateDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Address;
import de.allcom.models.User;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.UserRepository;
import de.allcom.services.mail.EmailSender;
import de.allcom.services.mail.MailTemplatesUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final EmailSender emailSender;
    private final MailTemplatesUtil mailTemplatesUtil;

    public Page<UserWithAddressResponseDto> getAll(PageRequest pageRequest) {
        Page<Object[]> results = userRepository.findAllUsersWithAddresses(pageRequest);
        return results.map(result -> UserWithAddressResponseDto.from((User) result[0], (Address) result[1]));
    }

    public Page<UserWithAddressResponseDto> searchUsers(int limit, int skip, String searchQuery) {
        PageRequest pageRequest = PageRequest.of(skip, limit);

        Page<Object[]> results = userRepository.searchUsersWithAddresses(searchQuery, pageRequest);

        return results.map(result -> UserWithAddressResponseDto.from((User) result[0], (Address) result[1]));
    }

    public UserWithAddressResponseDto getUserProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = addressRepository.findByUser(user);
        return UserWithAddressResponseDto.from(user, address);
    }

    @Transactional
    public UserWithAddressResponseDto updateUser(UserWithAddressUpdateDto request, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found!"));
        final boolean isCheckedBefore = existingUser.isChecked();
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        existingUser.setCompanyName(request.getCompanyName());
        existingUser.setPosition(request.getPosition());
        existingUser.setTaxNumber(request.getTaxNumber());
        existingUser.setChecked(request.isChecked());
        existingUser.setBlocked(request.isBlocked());
        existingUser.setUpdateAt(LocalDateTime.now());

        if (request.isChecked() && !isCheckedBefore) {
            String html = mailTemplatesUtil.createActivatedMail(existingUser.getFirstName(),
                    existingUser.getLastName());
            emailSender.send(existingUser.getEmail(), "Ihr Konto aktiviert", html);
        }
        if (request.isBlocked()) {
            String html = mailTemplatesUtil.createBlocketedMail(existingUser.getFirstName(),
                    existingUser.getLastName());
            emailSender.send(existingUser.getEmail(), "Ihr Konto vorübergehend gesperrt", html);
        }

        User savedUser = userRepository.save(existingUser);

        Optional<Address> optionalAddress = Optional.ofNullable(addressRepository.findByUser(savedUser));

        Address address = optionalAddress.orElseGet(Address::new);

        AddressDto addressDto = request.getAddress();

        address.setPostIndex(addressDto.getPostIndex());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setHouseNumber(addressDto.getHouseNumber());
        address.setUser(savedUser);

        addressRepository.saveAndFlush(address);
        return UserWithAddressResponseDto.from(savedUser, address);
    }

    public UserWithAddressResponseDto changeCredentialStatus(Long userId, boolean isChecked) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found!"));
        boolean isCheckedBefore = user.isChecked();
        user.setChecked(isChecked);
        userRepository.save(user);

        if (isChecked && !isCheckedBefore) {
            String html = mailTemplatesUtil.createActivatedMail(user.getFirstName(), user.getLastName());
            emailSender.send(user.getEmail(), "Ihr Konto aktiviert", html);
        }

        return UserWithAddressResponseDto.from(user, addressRepository.findByUser(user));
    }

    public UserWithAddressResponseDto changeBlockedStatus(Long userId, boolean isBlocked) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found!"));
        user.setBlocked(isBlocked);
        userRepository.save(user);

        if (isBlocked) {
            String html = mailTemplatesUtil.createBlocketedMail(user.getFirstName(), user.getLastName());
            emailSender.send(user.getEmail(), "Ihr Konto vorübergehend gesperrt", html);
        }

        return UserWithAddressResponseDto.from(user, addressRepository.findByUser(user));
    }

    public UserWithAddressResponseDto foundUserByEmail(String userEmail) {
        User user = (User) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "User with email " + userEmail + " not found!"));
        Address address = addressRepository.findByUser(user);
        return UserWithAddressResponseDto.from(user, address);
    }

    public UserWithAddressResponseDto foundUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " not found!"));
        Address address = addressRepository.findByUser(user);
        return UserWithAddressResponseDto.from(user, address);
    }

}
