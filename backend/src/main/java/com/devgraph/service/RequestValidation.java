package com.devgraph.service;

import org.springframework.util.StringUtils;

import com.devgraph.exception.InvalidRequestException;

/**
 * Shared input-validation helpers for the graph query services.
 */
final class RequestValidation {

    private RequestValidation() {
    }

    static void requireText(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new InvalidRequestException(fieldName + " must not be blank");
        }
    }

    static int resolveBounded(Integer requested, int defaultValue, int min, int max, String fieldName) {
        if (requested == null) {
            return defaultValue;
        }
        if (requested < min || requested > max) {
            throw new InvalidRequestException(
                    "%s must be between %d and %d, got %d".formatted(fieldName, min, max, requested));
        }
        return requested;
    }

    static int resolveOffset(Integer requested) {
        if (requested == null) {
            return 0;
        }
        if (requested < 0) {
            throw new InvalidRequestException("offset must not be negative, got " + requested);
        }
        return requested;
    }
}
