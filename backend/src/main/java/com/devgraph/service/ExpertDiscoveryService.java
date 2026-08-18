package com.devgraph.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.dto.ExpertPathResponse;
import com.devgraph.dto.PathNode;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Developer;
import com.devgraph.model.Skill;
import com.devgraph.repository.DeveloperRepository;
import com.devgraph.repository.ExpertDiscoveryRepository;
import com.devgraph.repository.ExpertDiscoveryRepository.NearestExpertPath;

/**
 * Feature 5: Nearest Expert Discovery
 * Developer -> [KNOWS ...] -> nearest colleague who holds a given skill
 */
@Service
public class ExpertDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(ExpertDiscoveryService.class);

    private static final int DEFAULT_MAX_HOPS = 6;
    private static final int MIN_HOPS = 1;
    private static final int MAX_HOPS = 10;

    @Autowired
    private ExpertDiscoveryRepository expertDiscoveryRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    public ExpertPathResponse findNearestExpert(String developerId, String skillId, Integer maxHops) {
        RequestValidation.requireText(developerId, "developerId");
        RequestValidation.requireText(skillId, "skillId");
        int effectiveMaxHops = RequestValidation.resolveBounded(maxHops, DEFAULT_MAX_HOPS, MIN_HOPS, MAX_HOPS, "maxHops");

        if (!expertDiscoveryRepository.developerExists(developerId)) {
            throw new ResourceNotFoundException("Developer not found: " + developerId);
        }
        Skill skill = expertDiscoveryRepository.findSkill(skillId);
        if (skill == null) {
            throw new ResourceNotFoundException("Skill not found: " + skillId);
        }

        if (expertDiscoveryRepository.developerHasSkillDirectly(developerId, skillId)) {
            return selfMatch(developerId, skill);
        }

        List<String> candidateIds = expertDiscoveryRepository.findSkillHolderIds(developerId, skillId);
        if (candidateIds.isEmpty()) {
            log.debug("No developer other than {} holds skill {}", developerId, skillId);
            return ExpertPathResponse.notFound(skill);
        }

        NearestExpertPath path = expertDiscoveryRepository.findNearestExpertPath(developerId, candidateIds, effectiveMaxHops);
        if (path == null) {
            log.debug("No KNOWS chain within {} hops from {} reaches a holder of skill {}", effectiveMaxHops, developerId, skillId);
            return ExpertPathResponse.notFound(skill);
        }

        log.debug("Nearest expert for skill {} from developer {}: {} hops", skillId, developerId, path.hops());
        return new ExpertPathResponse(true, path.nodes(), path.relationshipTypes(), path.hops(), skill);
    }

    private ExpertPathResponse selfMatch(String developerId, Skill skill) {
        Developer developer = developerRepository.findById(developerId).orElseThrow(
                () -> new ResourceNotFoundException("Developer not found: " + developerId));
        PathNode self = new PathNode("Developer", developer.id(), developer.name());
        return new ExpertPathResponse(true, List.of(self), List.of(), 0, skill);
    }
}
