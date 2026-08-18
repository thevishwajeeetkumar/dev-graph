package com.devgraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devgraph.dto.ApiResponse;
import com.devgraph.dto.HealthStatus;
import com.devgraph.service.HealthService;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    @GetMapping
    public ResponseEntity<ApiResponse<HealthStatus>> health() {
        HealthStatus status = healthService.checkHealth();
        HttpStatus httpStatus = status.isUp() ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(httpStatus).body(ApiResponse.success(status));
    }
}
