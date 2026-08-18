package com.devgraph.exception;

import org.springframework.http.HttpStatus;

/**
 * Base for errors originating from CognoDB itself, as opposed to bad client
 * input. Repositories translate raw {@code Neo4jException}s into one of
 * this hierarchy's concrete subclasses at the driver boundary, so the rest
 * of the application never depends on driver-specific exception types.
 */
public abstract class GraphDatabaseException extends DevGraphException {
    protected GraphDatabaseException(HttpStatus httpStatus, String message, Throwable cause) {
        super(httpStatus, message, cause);
    }
}
