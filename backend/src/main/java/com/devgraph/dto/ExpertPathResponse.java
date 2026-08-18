package com.devgraph.dto;

import java.util.List;

import com.devgraph.model.Skill;

public record ExpertPathResponse(
        boolean pathFound,
        List<PathNode> nodes,
        List<String> relationshipTypes,
        int hops,
        Skill matchedSkill) {

    public static ExpertPathResponse notFound(Skill skill) {
        return new ExpertPathResponse(false, List.of(), List.of(), 0, skill);
    }
}
