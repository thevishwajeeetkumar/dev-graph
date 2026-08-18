package com.devgraph.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.dto.HiddenSkillRecord;
import com.devgraph.dto.HiddenSkillResponse;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.repository.HiddenSkillRepository;

/**
 * Feature 3: Hidden Skill Discovery
 * Developer -> Project -> Technology (not listed in skills)
 */
@Service
public class HiddenSkillService {

    private static final Logger log = LoggerFactory.getLogger(HiddenSkillService.class);

    private static final int DEFAULT_LIMIT = 25;
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 100;

    @Autowired
    private HiddenSkillRepository hiddenSkillRepository;

    public HiddenSkillResponse discoverHiddenSkills(String developerId, Integer offset, Integer limit) {
        RequestValidation.requireText(developerId, "developerId");
        int effectiveOffset = RequestValidation.resolveOffset(offset);
        int effectiveLimit = RequestValidation.resolveBounded(limit, DEFAULT_LIMIT, MIN_LIMIT, MAX_LIMIT, "limit");

        if (!hiddenSkillRepository.developerExists(developerId)) {
            throw new ResourceNotFoundException("Developer not found: " + developerId);
        }

        List<HiddenSkillRecord> hiddenSkills =
                hiddenSkillRepository.findHiddenSkills(developerId, effectiveOffset, effectiveLimit);
        log.debug("Found {} hidden skill(s) for developer {} (offset={}, limit={})",
                hiddenSkills.size(), developerId, effectiveOffset, effectiveLimit);
        return new HiddenSkillResponse(developerId, hiddenSkills, effectiveOffset, effectiveLimit, hiddenSkills.size());
    }
}
