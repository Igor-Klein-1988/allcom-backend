package de.allcom.exceptions;

import de.allcom.dto.StandardResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<StandardResponseDto> handleRestException(RestException e) {
        log.info(e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(StandardResponseDto.builder().message(e.getMessage()).build());
    }

}
