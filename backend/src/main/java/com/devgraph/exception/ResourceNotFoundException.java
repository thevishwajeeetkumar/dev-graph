package com.devgraph.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends DevGraphException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
