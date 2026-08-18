package com.devgraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devgraph.dto.ApiResponse;
import com.devgraph.dto.OverviewStats;
import com.devgraph.service.StatsService;

@RestController
@RequestMapping("/api/stats")
@Validated
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/overview")
    public ApiResponse<OverviewStats> getOverviewStats(@RequestParam(required = false) Integer topSkillsLimit) {
        return ApiResponse.success(statsService.getOverviewStats(topSkillsLimit));
    }
}
