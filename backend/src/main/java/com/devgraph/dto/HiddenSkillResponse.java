package com.devgraph.dto;

import java.util.List;

public record HiddenSkillResponse(
    String developerId,
    List<HiddenSkillRecord> hiddenSkills,
    int offset,
    int limit,
    int resultCount) {
}
