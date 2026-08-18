package com.devgraph.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.devgraph.dto.ApiError;
import com.devgraph.dto.ApiResponse;

import jakarta.validation.ConstraintViolationException;

/**
 * Translates every exception that can escape a controller into the same
 * {@link ApiResponse} envelope. Raw driver/library exception details are
 * logged server-side only; clients never see a stack trace or an internal
 * message they didn't cause.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Every domain exception (not-found, invalid request, graph database
     * unavailable/errored) carries its own HTTP status, so one handler
     * covers the whole hierarchy.
     */
    @ExceptionHandler(DevGraphException.class)
    public ResponseEntity<ApiResponse<Void>> handleDevGraphException(DevGraphException ex) {
        HttpStatus status = ex.getHttpStatus();
        if (status.is5xxServerError()) {
            log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        } else {
            log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
        return respond(status, ex.getMessage());
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleValidation(Exception ex) {
        log.warn("Rejected malformed request: {}", ex.getMessage());
        return respond(HttpStatus.BAD_REQUEST, "The request could not be processed: " + ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFound(NoHandlerFoundException ex) {
        return respond(HttpStatus.NOT_FOUND, "No endpoint matches " + ex.getHttpMethod() + " " + ex.getRequestURL());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return respond(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    private ResponseEntity<ApiResponse<Void>> respond(HttpStatus status, String message) {
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), message);
        return ResponseEntity.status(status).body(ApiResponse.failure(error));
    }
}
