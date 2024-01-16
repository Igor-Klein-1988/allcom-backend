package de.allcom.services.auth;


import de.allcom.controllers.auth.AuthentificationRequest;
import de.allcom.controllers.auth.AuthentificationResponse;
import de.allcom.dto.user.UserAddressRegistrationDto;
import de.allcom.models.Address;
import de.allcom.models.Role;
import de.allcom.models.User;
import de.allcom.models.token.Token;
import de.allcom.models.token.TokenType;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.TokenRepository;
import de.allcom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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
                .token(jwtToken)
                .build();
    }

    public AuthentificationResponse login(AuthentificationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository
                .findUserByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        savedUserToken(user, jwtToken);
        return AuthentificationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private  void revokeAllUserTokens(User user) {
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
