package de.allcom.exceptions;

import de.allcom.dto.StandardResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = RestException.class)
    public ResponseEntity<StandardResponseDto> handleRestException(RestException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(StandardResponseDto.builder()
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<StandardResponseDto> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(StandardResponseDto.builder()
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(value = ServletException.class)
    public ResponseEntity<StandardResponseDto> handleServletException(ServletException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(StandardResponseDto.builder()
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<StandardResponseDto> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(StandardResponseDto.builder()
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
