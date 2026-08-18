package com.devgraph.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a request is well-formed but semantically invalid for the
 * graph query being run (e.g. an out-of-range limit, or a source and target
 * that are the same node).
 */
public class InvalidRequestException extends DevGraphException {
    public InvalidRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
