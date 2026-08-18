package com.devgraph.dto;

import java.time.Instant;

/**
 * Uniform envelope for every REST response, success or failure, so clients
 * only ever have to check {@code success} instead of branching on status
 * codes and different body shapes.
 */
public record ApiResponse<T>(boolean success, T data, ApiError error, Instant timestamp) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static ApiResponse<Void> failure(ApiError error) {
        return new ApiResponse<>(false, null, error, Instant.now());
    }
}
