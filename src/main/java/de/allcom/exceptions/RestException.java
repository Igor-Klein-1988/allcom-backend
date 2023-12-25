package de.allcom.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private final HttpStatus status;

    public RestException(HttpStatus httpStatus, String message) {
        super(message);
        this.status = httpStatus;
    }
}
