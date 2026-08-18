package com.devgraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devgraph.dto.ApiResponse;
import com.devgraph.dto.ConnectionDiscoveryResponse;
import com.devgraph.dto.ExpertPathResponse;
import com.devgraph.dto.HiddenSkillResponse;
import com.devgraph.dto.ShortestPathResponse;
import com.devgraph.dto.TalentBridgeResponse;
import com.devgraph.service.ConnectionDiscoveryService;
import com.devgraph.service.ExpertDiscoveryService;
import com.devgraph.service.HiddenSkillService;
import com.devgraph.service.ShortestPathService;
import com.devgraph.service.TalentBridgeService;

import jakarta.validation.constraints.NotBlank;

/**
 * Endpoints for the five DevGraph power features. No business logic or
 * Cypher lives here - each method only validates transport-level input
 * shape (via bean validation) and delegates to its service.
 */
@RestController
@RequestMapping("/api/graph")
@Validated
public class GraphController {

    @Autowired
    private ConnectionDiscoveryService connectionDiscoveryService;

    @Autowired
    private ShortestPathService shortestPathService;

    @Autowired
    private HiddenSkillService hiddenSkillService;

    @Autowired
    private TalentBridgeService talentBridgeService;

    @Autowired
    private ExpertDiscoveryService expertDiscoveryService;

    /**
     * Feature 1: Professional Connection Discovery.
     * Developer -> Project -> Developer -> Company
     */
    @GetMapping("/connections")
    public ApiResponse<ConnectionDiscoveryResponse> discoverConnections(
            @RequestParam @NotBlank String developerId,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit) {
        return ApiResponse.success(connectionDiscoveryService.discoverConnections(developerId, offset, limit));
    }

    /**
     * Feature 2: Shortest Connection Path.
     * Developer -> Developer -> Developer -> Company
     */
    @GetMapping("/shortest-path")
    public ApiResponse<ShortestPathResponse> findShortestPath(
            @RequestParam @NotBlank String developerId,
            @RequestParam @NotBlank String companyId,
            @RequestParam(required = false) Integer maxHops) {
        return ApiResponse.success(shortestPathService.findShortestPath(developerId, companyId, maxHops));
    }

    /**
     * Feature 3: Hidden Skill Discovery.
     * Developer -> Project -> Technology (not listed in skills)
     */
    @GetMapping("/hidden-skills")
    public ApiResponse<HiddenSkillResponse> discoverHiddenSkills(
            @RequestParam @NotBlank String developerId,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit) {
        return ApiResponse.success(hiddenSkillService.discoverHiddenSkills(developerId, offset, limit));
    }

    /**
     * Feature 4: Talent Bridge Discovery.
     * Company A -> Developer -> Developer -> Company B
     */
    @GetMapping("/talent-bridge")
    public ApiResponse<TalentBridgeResponse> discoverTalentBridges(
            @RequestParam @NotBlank String companyAId,
            @RequestParam @NotBlank String companyBId,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit) {
        return ApiResponse.success(talentBridgeService.discoverBridges(companyAId, companyBId, offset, limit));
    }

    /**
     * Feature 5: Nearest Expert Discovery.
     * Developer -> [KNOWS ...] -> nearest colleague who holds a given skill
     */
    @GetMapping("/nearest-expert")
    public ApiResponse<ExpertPathResponse> findNearestExpert(
            @RequestParam @NotBlank String developerId,
            @RequestParam @NotBlank String skillId,
            @RequestParam(required = false) Integer maxHops) {
        return ApiResponse.success(expertDiscoveryService.findNearestExpert(developerId, skillId, maxHops));
    }
}
