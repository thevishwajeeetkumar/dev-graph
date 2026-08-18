package com.devgraph.exception;

import org.springframework.http.HttpStatus;

/**
 * Base of the DevGraph exception hierarchy. Every subclass carries the HTTP
 * status it should be reported as, so {@code GlobalExceptionHandler} can
 * translate any of them with a single handler instead of one per type.
 */
public abstract class DevGraphException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected DevGraphException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    protected DevGraphException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
