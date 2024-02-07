package de.allcom.config.security;

import de.allcom.exceptions.SecurityExceptionHandlers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static de.allcom.models.Role.ADMIN;
import static de.allcom.models.Role.CLIENT;
import static de.allcom.models.Role.STOREKEEPER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@ComponentScan("de.allcom.config.security")
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/",
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/logout",
            "/api/auth/register",
            "/api/categories/**",
            "/api/products/search",
            "/api/products/{productId}",
            "/ws/**",

            "/api-docs",
            "/api-docs/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/api/auth/changePassword").hasAnyAuthority(
                                ADMIN.name(), CLIENT.name(), STOREKEEPER.name())
                        .requestMatchers("/api/users/getAll").hasAuthority(ADMIN.name())
                        .requestMatchers("/api/users/updateUser/**").hasAuthority(ADMIN.name())
                        .requestMatchers("/api/users/getUserProfile").hasAnyAuthority(
                                ADMIN.name(), CLIENT.name(), STOREKEEPER.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext();
                            SecurityExceptionHandlers.LOGOUT_SUCCESS_HANDLER
                                    .onLogoutSuccess(request, response,
                                            authentication);
                        }))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(SecurityExceptionHandlers.ENTRY_POINT,
                                new AntPathRequestMatcher("/api/**"))
                        .accessDeniedHandler(SecurityExceptionHandlers.ACCESS_DENIED_HANDLER));
        return http.build();
    }
}
