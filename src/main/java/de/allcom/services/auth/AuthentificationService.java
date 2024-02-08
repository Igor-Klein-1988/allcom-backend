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
import de.allcom.models.Wishlist;
import de.allcom.models.token.Token;
import de.allcom.models.token.TokenType;
import de.allcom.repositories.AddressRepository;
import de.allcom.repositories.TokenRepository;
import de.allcom.repositories.UserRepository;
import de.allcom.repositories.WishlistRepository;
import de.allcom.services.mail.EmailSender;
import de.allcom.services.mail.MailTemplatesUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

@Service
@RequiredArgsConstructor
public class AuthentificationService {
    @Value("${jwt.lifetime}")
    private long jwtLifetime;

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailSender emailSender;

    private final MailTemplatesUtil mailTemplatesUtil;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final WishlistRepository wishlistRepository;


    @Transactional
    public AuthentificationResponseDto register(UserWithAddressRegistrationDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RestException(HttpStatus.CONFLICT, "User with email " + request.getEmail() + " already exists!");
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
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(savedUser);
        wishlistRepository.save(wishlist);

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

        String html = mailTemplatesUtil.createRegistrationMail(user.getFirstName(), user.getLastName());

        emailSender.send(user.getEmail(), "Registrierung auf der Allcom-Website", html);

        String jwtToken = jwtService.generateToken(user);
        savedUserToken(savedUser, jwtToken);
        return AuthentificationResponseDto.builder()
                                          .id(savedUser.getId())
                                          .firstName(savedUser.getFirstName())
                                          .lastName(savedUser.getLastName())
                                          .email(savedUser.getEmail())
                                          .role(savedUser.getRole().toString())
                                          .token(jwtToken)
                                          .build();
    }

    public AuthentificationResponseDto login(AuthentificationRequestDto request, HttpServletResponse response) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Email and password are required!");
        } else if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new RestException(HttpStatus.UNAUTHORIZED,
                    "User with email " + request.getEmail() + " not found!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "User with email " + request.getEmail() + " not found!"));
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        savedUserToken(user, jwtToken);

        Cookie authCookie = new Cookie("authorization", jwtToken);
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge((int) jwtLifetime); // Время жизни куки в секундах
        authCookie.setPath("/");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.addCookie(authCookie);
        return AuthentificationResponseDto.builder()
                                          .id(user.getId())
                                          .firstName(user.getFirstName())
                                          .lastName(user.getLastName())
                                          .email(user.getEmail())
                                          .role(user.getRole().toString())
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

    public AuthentificationResponseDto checkAuth(HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "authorization");
        if (authCookie == null) {
            return AuthentificationResponseDto.builder().isAuthenticated(false).build();
        }

        String token = authCookie.getValue();
        try {
            String userEmail = jwtService.extractUsername(token);
            if (userEmail == null || userEmail.isEmpty()) {
                return AuthentificationResponseDto.builder().isAuthenticated(false).build();
            }

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(token)
                                              .map(t -> !t.isExpired() && !t.isRevoked())
                                              .orElse(false);
            if (jwtService.isTokenValid(token, userDetails) && isTokenValid) {
                return userRepository.findByEmail(userEmail)
                                     .map(user -> AuthentificationResponseDto.builder()
                                                                             .isAuthenticated(true)
                                                                             .id(user.getId())
                                                                             .firstName(user.getFirstName())
                                                                             .lastName(user.getLastName())
                                                                             .email(user.getEmail())
                                                                             .role(user.getRole().toString())
                                                                             .build())
                                     .orElse(AuthentificationResponseDto.builder().isAuthenticated(false).build());
            }

            return AuthentificationResponseDto.builder().isAuthenticated(false).build();
        } catch (Exception e) {
            throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid token: " + e.getMessage());
        }
    }
}
