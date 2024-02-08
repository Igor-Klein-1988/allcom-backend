package de.allcom.exceptions;

import de.allcom.dto.StandardResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<StandardResponseDto> handleRestException(RestException e) {
        log.info(e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(
                StandardResponseDto.builder()
                        .message(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("'%s' should be a valid '%s' and '%s' isn't",
                name, type, value);

        System.out.println(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(StandardResponseDto.builder().message(ex.getMessage()).build());
    }
}
