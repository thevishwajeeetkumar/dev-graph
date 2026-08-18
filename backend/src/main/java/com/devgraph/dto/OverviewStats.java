package com.devgraph.dto;

import java.util.List;

public record OverviewStats(
    long developerCount,
    long companyCount,
    long skillCount,
    long projectCount,
    List<SkillUsage> topSkills) {
}
