package com.devgraph.dto;

public record ApiError(int status, String error, String message) {
}
