package com.devgraph.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.dto.TalentBridgeRecord;
import com.devgraph.dto.TalentBridgeResponse;
import com.devgraph.exception.InvalidRequestException;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.repository.TalentBridgeRepository;

/**
 * Feature 4: Talent Bridge Discovery
 * Company A -> Developer -> Developer -> Company B
 */
@Service
public class TalentBridgeService {

    private static final Logger log = LoggerFactory.getLogger(TalentBridgeService.class);

    private static final int DEFAULT_LIMIT = 25;
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 100;

    @Autowired
    private TalentBridgeRepository talentBridgeRepository;

    public TalentBridgeResponse discoverBridges(String companyAId, String companyBId, Integer offset, Integer limit) {
        RequestValidation.requireText(companyAId, "companyAId");
        RequestValidation.requireText(companyBId, "companyBId");
        if (companyAId.equals(companyBId)) {
            throw new InvalidRequestException("companyAId and companyBId must be different companies");
        }
        int effectiveOffset = RequestValidation.resolveOffset(offset);
        int effectiveLimit = RequestValidation.resolveBounded(limit, DEFAULT_LIMIT, MIN_LIMIT, MAX_LIMIT, "limit");

        if (!talentBridgeRepository.companyExists(companyAId)) {
            throw new ResourceNotFoundException("Company not found: " + companyAId);
        }
        if (!talentBridgeRepository.companyExists(companyBId)) {
            throw new ResourceNotFoundException("Company not found: " + companyBId);
        }

        List<TalentBridgeRecord> bridges =
                talentBridgeRepository.findBridges(companyAId, companyBId, effectiveOffset, effectiveLimit);
        log.debug("Found {} talent bridge(s) between {} and {} (offset={}, limit={})",
                bridges.size(), companyAId, companyBId, effectiveOffset, effectiveLimit);
        return new TalentBridgeResponse(companyAId, companyBId, bridges, effectiveOffset, effectiveLimit, bridges.size());
    }
}
