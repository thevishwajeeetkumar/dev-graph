package com.devgraph.config;

import org.neo4j.driver.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Verifies CognoDB connectivity once at startup and logs a clear
 * PASSED/FAILED line - so "is the database actually reachable" is visible
 * immediately in the startup log instead of only surfacing on the first
 * request. Deliberately does not fail startup on error: the readiness
 * probe ({@link CognoDbHealthIndicator}) is what gates traffic, so a
 * database that's merely slow to come up (e.g. in docker-compose) shouldn't
 * crash-loop the app.
 */
@Component
public class CognoDbConnectivityLogger {

    private static final Logger log = LoggerFactory.getLogger(CognoDbConnectivityLogger.class);

    @Autowired
    private Driver driver;

    @Value("${cognodb.uri}")
    private String uri;

    @EventListener(ApplicationReadyEvent.class)
    public void verifyOnStartup() {
        long start = System.currentTimeMillis();
        try {
            driver.verifyConnectivity();
            log.info("CognoDB connectivity check PASSED - connected to {} ({} ms)", uri, System.currentTimeMillis() - start);
        } catch (Exception ex) {
            log.error("CognoDB connectivity check FAILED - could not reach {} ({}: {})",
                    uri, ex.getClass().getSimpleName(), ex.getMessage());
        }
    }
}
