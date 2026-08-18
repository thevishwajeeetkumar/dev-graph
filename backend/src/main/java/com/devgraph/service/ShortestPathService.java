package com.devgraph.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.dto.ShortestPathResponse;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.repository.ShortestPathRepository;

/**
 * Feature 2: Shortest Connection Path
 * Developer -> Developer -> Developer -> Company
 */
@Service
public class ShortestPathService {

    private static final Logger log = LoggerFactory.getLogger(ShortestPathService.class);

    private static final int DEFAULT_MAX_HOPS = 6;
    private static final int MIN_HOPS = 1;
    private static final int MAX_HOPS = 10;

    @Autowired
    private ShortestPathRepository shortestPathRepository;

    public ShortestPathResponse findShortestPath(String developerId, String companyId, Integer maxHops) {
        RequestValidation.requireText(developerId, "developerId");
        RequestValidation.requireText(companyId, "companyId");
        int effectiveMaxHops = RequestValidation.resolveBounded(maxHops, DEFAULT_MAX_HOPS, MIN_HOPS, MAX_HOPS, "maxHops");

        if (!shortestPathRepository.developerExists(developerId)) {
            throw new ResourceNotFoundException("Developer not found: " + developerId);
        }
        if (!shortestPathRepository.companyExists(companyId)) {
            throw new ResourceNotFoundException("Company not found: " + companyId);
        }

        ShortestPathResponse response = shortestPathRepository.findShortestPath(developerId, companyId, effectiveMaxHops);
        log.debug("Shortest path from developer {} to company {}: found={}, hops={}",
                developerId, companyId, response.pathFound(), response.hops());
        return response;
    }
}
