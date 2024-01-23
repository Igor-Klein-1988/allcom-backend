package de.allcom.services.auth;


import de.allcom.controllers.auth.AuthentificationRequest;
import de.allcom.controllers.auth.AuthentificationResponse;
import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Address;
import de.allcom.models.Role;
import de.allcom.models.User;
import de.allcom.models.token.Token;
import de.allcom.models.token.TokenType;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.TokenRepository;
import de.allcom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthentificationService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthentificationResponse register(UserAddressRegistrationDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RestException(HttpStatus.CONFLICT,
                    "User with email " + request.getEmail() + " already exists!");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .position(request.getPosition())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .taxNumber(request.getTaxNumber())
                .role(Role.CLIENT)
                .build();

        var savedUser = userRepository.save(user);

        var address = Address.builder()
                .postIndex(request.getPostIndex())
                .city(request.getCity())
                .street(request.getStreet())
                .houseNumber(request.getHouseNumber())
                .user(savedUser)
                .build();

        addressRepository.save(address);

        var jwtToken = jwtService.generateToken(user);
        savedUserToken(savedUser, jwtToken);
        return AuthentificationResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .token(jwtToken)
                .build();
    }

    public AuthentificationResponse login(AuthentificationRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "Email and password are required!");
        } else if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND,
                    "User with email " + request.getEmail() + " not found!");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        var user = (User) userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "User with email " + request.getEmail() + " not found!"));
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        savedUserToken(user, jwtToken);
        return AuthentificationResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
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
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
