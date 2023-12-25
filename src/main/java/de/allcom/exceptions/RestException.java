package org.demointernetshop.exceptions;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
    private final HttpStatus status;

    public RestException(HttpStatus httpStatus, String message) {
        super(message);
        this.status = httpStatus;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
