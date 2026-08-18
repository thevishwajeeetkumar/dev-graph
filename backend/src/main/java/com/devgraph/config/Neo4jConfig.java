package com.devgraph.config;

import java.util.concurrent.TimeUnit;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CognoDB is a graph database exposed over the Bolt 5.x protocol and is
 * accessed through the official Neo4j Java driver. Connection details are
 * sourced from environment variables so credentials never live in source
 * control.
 */
@Configuration
public class Neo4jConfig {

    private static final Logger log = LoggerFactory.getLogger(Neo4jConfig.class);

    @Value("${cognodb.uri}")
    private String uri;

    @Value("${cognodb.username}")
    private String username;

    @Value("${cognodb.password}")
    private String password;

    @Value("${cognodb.pool.max-connection-pool-size}")
    private int maxConnectionPoolSize;

    @Value("${cognodb.pool.connection-acquisition-timeout-seconds}")
    private long connectionAcquisitionTimeoutSeconds;

    @Value("${cognodb.pool.max-connection-lifetime-minutes}")
    private long maxConnectionLifetimeMinutes;

    @Value("${cognodb.pool.connection-timeout-seconds}")
    private long connectionTimeoutSeconds;

    @Bean(destroyMethod = "close")
    public Driver neo4jDriver() {
        log.info("Connecting to CognoDB at {} (pool size={}, acquisitionTimeout={}s)",
                uri, maxConnectionPoolSize, connectionAcquisitionTimeoutSeconds);

        Config config = Config.builder()
                .withMaxConnectionPoolSize(maxConnectionPoolSize)
                .withConnectionAcquisitionTimeout(connectionAcquisitionTimeoutSeconds, TimeUnit.SECONDS)
                .withMaxConnectionLifetime(maxConnectionLifetimeMinutes, TimeUnit.MINUTES)
                .withConnectionTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
                .build();

        return GraphDatabase.driver(uri, AuthTokens.basic(username, password), config);
    }
}
