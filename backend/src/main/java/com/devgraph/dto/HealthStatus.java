package com.devgraph.dto;

public record HealthStatus(String status, String database, long checkDurationMillis) {

    public boolean isUp() {
        return "UP".equals(status);
    }
}
