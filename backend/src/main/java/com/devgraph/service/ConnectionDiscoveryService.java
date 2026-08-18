package com.devgraph.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.dto.ConnectionDiscoveryResponse;
import com.devgraph.dto.ConnectionRecord;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.repository.ConnectionDiscoveryRepository;

/**
 * Feature 1: Professional Connection Discovery
 * Developer -> Project -> Developer -> Company
 */
@Service
public class ConnectionDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(ConnectionDiscoveryService.class);

    private static final int DEFAULT_LIMIT = 25;
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 100;

    @Autowired
    private ConnectionDiscoveryRepository connectionDiscoveryRepository;

    public ConnectionDiscoveryResponse discoverConnections(String developerId, Integer offset, Integer limit) {
        RequestValidation.requireText(developerId, "developerId");
        int effectiveOffset = RequestValidation.resolveOffset(offset);
        int effectiveLimit = RequestValidation.resolveBounded(limit, DEFAULT_LIMIT, MIN_LIMIT, MAX_LIMIT, "limit");

        if (!connectionDiscoveryRepository.developerExists(developerId)) {
            throw new ResourceNotFoundException("Developer not found: " + developerId);
        }

        List<ConnectionRecord> connections =
                connectionDiscoveryRepository.findConnections(developerId, effectiveOffset, effectiveLimit);
        log.debug("Found {} connection(s) for developer {} (offset={}, limit={})",
                connections.size(), developerId, effectiveOffset, effectiveLimit);
        return new ConnectionDiscoveryResponse(developerId, connections, effectiveOffset, effectiveLimit, connections.size());
    }
}
