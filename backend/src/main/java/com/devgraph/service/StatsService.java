package com.devgraph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.dto.OverviewStats;
import com.devgraph.repository.StatsRepository;

@Service
public class StatsService {

    private static final int DEFAULT_TOP_SKILLS_LIMIT = 5;
    private static final int MIN_TOP_SKILLS_LIMIT = 1;
    private static final int MAX_TOP_SKILLS_LIMIT = 50;

    @Autowired
    private StatsRepository statsRepository;

    public OverviewStats getOverviewStats(Integer topSkillsLimit) {
        int effectiveLimit = RequestValidation.resolveBounded(
                topSkillsLimit, DEFAULT_TOP_SKILLS_LIMIT, MIN_TOP_SKILLS_LIMIT, MAX_TOP_SKILLS_LIMIT, "topSkillsLimit");

        return new OverviewStats(
                statsRepository.countDevelopers(),
                statsRepository.countCompanies(),
                statsRepository.countSkills(),
                statsRepository.countProjects(),
                statsRepository.findTopSkills(effectiveLimit)
        );
    }
}
