package com.devgraph.service;

import org.neo4j.driver.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.dto.HealthStatus;

/**
 * Verifies CognoDB connectivity for the health endpoint. Deliberately talks
 * to the driver directly rather than through a repository: this is an
 * infrastructure check (is the database reachable), not a Cypher query.
 */
@Service
public class HealthService {

    private static final Logger log = LoggerFactory.getLogger(HealthService.class);

    @Autowired
    private Driver driver;

    public HealthStatus checkHealth() {
        long start = System.currentTimeMillis();
        try {
            driver.verifyConnectivity();
            return new HealthStatus("UP", "UP", System.currentTimeMillis() - start);
        } catch (Exception ex) {
            log.error("Health check failed: CognoDB is unreachable", ex);
            return new HealthStatus("DOWN", "DOWN", System.currentTimeMillis() - start);
        }
    }
}
