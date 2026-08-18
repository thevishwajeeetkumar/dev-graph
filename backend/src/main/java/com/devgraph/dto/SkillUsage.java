package com.devgraph.dto;

import com.devgraph.model.Skill;

/**
 * A skill ranked by how many developers declare it via {@code HAS_SKILL},
 * used for the Overview dashboard's "Top Skills" widget.
 */
public record SkillUsage(Skill skill, long developerCount) {
}
