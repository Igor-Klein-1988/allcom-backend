package de.allcom.services.auth;

import de.allcom.dto.StandardResponseDto;
import de.allcom.dto.auth.AuthentificationRequestDto;
import de.allcom.dto.auth.AuthentificationResponseDto;
import de.allcom.dto.auth.ChangePasswordRequestDto;
import de.allcom.dto.user.AddressDto;
import de.allcom.dto.user.UserWithAddressRegistrationDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Address;
import de.allcom.models.Role;
import de.allcom.models.User;
import de.allcom.models.token.Token;
import de.allcom.models.token.TokenType;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.TokenRepository;
import de.allcom.repositories.UserRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthentificationService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthentificationResponseDto register(UserWithAddressRegistrationDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RestException(HttpStatus.CONFLICT,
                    "User with email " + request.getEmail() + " already exists!");
        }
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .position(request.getPosition())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .taxNumber(request.getTaxNumber())
                .role(Role.CLIENT)
                .createAt(now)
                .updateAt(now)
                .build();

        User savedUser = userRepository.save(user);

        AddressDto addressDto = request.getAddress();
        if (addressDto != null) {
            Address address = Address.builder()
                    .postIndex(addressDto.getPostIndex())
                    .city(addressDto.getCity())
                    .street(addressDto.getStreet())
                    .houseNumber(addressDto.getHouseNumber())
                    .user(savedUser)
                    .build();
            addressRepository.save(address);
        }

        String jwtToken = jwtService.generateToken(user);
        savedUserToken(savedUser, jwtToken);
        return AuthentificationResponseDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .token(jwtToken)
                .build();
    }

    public AuthentificationResponseDto login(AuthentificationRequestDto request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "Email and password are required!");
        } else if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND,
                    "User with email " + request.getEmail() + " not found!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = (User) userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "User with email " + request.getEmail() + " not found!"));
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        savedUserToken(user, jwtToken);
        return AuthentificationResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void savedUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public StandardResponseDto changePassword(ChangePasswordRequestDto request, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RestException(HttpStatus.FORBIDDEN, "Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new RestException(HttpStatus.FORBIDDEN, "Password are not the same");
        }

        user.setHashPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
        return new StandardResponseDto("Password changed");
    }
}
