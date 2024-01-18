package de.allcom.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Getter
public class CopyPhotoException extends IOException {

    private final HttpStatus status;
    public CopyPhotoException(HttpStatus httpStatus, String message) {
        super(message);
        this.status = httpStatus;
    }
}
