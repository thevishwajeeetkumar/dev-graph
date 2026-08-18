package com.devgraph.dto;

import java.util.List;

import com.devgraph.model.Project;
import com.devgraph.model.Skill;

public record HiddenSkillRecord(
    Skill skill, 
    List<Project> projects) {
}
