package com.devgraph.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.devgraph.dto.HiddenSkillRecord;
import com.devgraph.model.Project;
import com.devgraph.model.Skill;

/**
 * Feature 3: Hidden Skill Discovery
 * Developer -> Project -> Technology (used in a project but not listed as a skill)
 *
 * <p>Deliberately does the skill-gap filtering in application code instead
 * of a single Cypher query. CognoDB's Cypher engine does not correctly
 * correlate an already-bound variable when it's reused as the *target* of a
 * later pattern in the same query - a {@code WHERE NOT (pattern)} or
 * {@code exists(pattern)} predicate, or an {@code OPTIONAL MATCH}, that
 * refers back to a node already bound earlier (e.g. {@code tech} from the
 * first MATCH) gets re-resolved as if it were unbound, producing wrong
 * (usually cartesian-multiplied) results instead of the correlated ones.
 * Two independent single-purpose reads plus a set difference in Java sidesteps
 * that entirely and is what's actually verified correct against the real
 * database - see the project history for how this was diagnosed.</p>
 */
@Repository
public class HiddenSkillRepository extends Neo4jRepositorySupport {

    private static final String FIND_PROJECT_SKILLS = """
            MATCH (developer:Developer {id: $developerId})-[:BUILT]->(project:Project)-[:USES]->(tech:Skill)
            RETURN tech, project
            ORDER BY tech.name
            """;

    private static final String FIND_KNOWN_SKILL_IDS = """
            MATCH (developer:Developer {id: $developerId})-[:HAS_SKILL]->(skill:Skill)
            RETURN skill.id AS skillId
            """;

    public boolean developerExists(String developerId) {
        return exists(NodeLabel.DEVELOPER, developerId);
    }

    public List<HiddenSkillRecord> findHiddenSkills(String developerId, int offset, int limit) {
        List<ProjectSkillRow> projectSkills = read(FIND_PROJECT_SKILLS, Map.of("developerId", developerId),
                record -> new ProjectSkillRow(
                        GraphRecordMapper.toSkill(record.get("tech").asNode()),
                        GraphRecordMapper.toProject(record.get("project").asNode())));

        Set<String> knownSkillIds = new HashSet<>(read(FIND_KNOWN_SKILL_IDS, Map.of("developerId", developerId),
                record -> record.get("skillId").asString()));

        Map<Skill, List<Project>> projectsBySkill = new java.util.LinkedHashMap<>();
        for (ProjectSkillRow row : projectSkills) {
            if (knownSkillIds.contains(row.skill().id())) {
                continue;
            }
            projectsBySkill.computeIfAbsent(row.skill(), unused -> new ArrayList<>()).add(row.project());
        }

        return projectsBySkill.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().name()))
                .skip(offset)
                .limit(limit)
                .map(entry -> new HiddenSkillRecord(entry.getKey(), List.copyOf(entry.getValue())))
                .collect(Collectors.toList());
    }

    private record ProjectSkillRow(Skill skill, Project project) {
    }
}
