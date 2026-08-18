package com.devgraph.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.devgraph.dto.HealthStatus;
import com.devgraph.service.HealthService;

/**
 * Contributes CognoDB connectivity to Spring Boot Actuator's health
 * infrastructure, registered as the "cognoDb" indicator (bean name minus
 * the "HealthIndicator" suffix). Included in the readiness probe group
 * (see application.yml) so an orchestrator stops routing traffic to this
 * instance the moment the database is unreachable, instead of only finding
 * out from a 503 on the next real request.
 */
@Component
public class CognoDbHealthIndicator implements HealthIndicator {

    @Autowired
    private HealthService healthService;

    @Override
    public Health health() {
        HealthStatus status = healthService.checkHealth();
        Health.Builder builder = status.isUp() ? Health.up() : Health.down();
        return builder.withDetail("responseTimeMs", status.checkDurationMillis()).build();
    }
}
