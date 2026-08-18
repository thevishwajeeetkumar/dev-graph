package com.devgraph.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.devgraph.dto.SkillUsage;

/**
 * Aggregate counts for the Overview dashboard. Node/relationship counts
 * reuse {@link Neo4jRepositorySupport#countNodes(NodeLabel)}; top skills is
 * a single grouped aggregation, no negation/OPTIONAL MATCH involved so it
 * isn't subject to the CognoDB rebound-variable quirk documented on
 * {@link HiddenSkillRepository} and {@link ShortestPathRepository}.
 */
@Repository
public class StatsRepository extends Neo4jRepositorySupport {

    private static final String TOP_SKILLS = """
            MATCH (s:Skill)<-[:HAS_SKILL]-(d:Developer)
            WITH s, count(d) AS developerCount
            RETURN s, developerCount
            ORDER BY developerCount DESC, s.name ASC
            LIMIT $limit
            """;

    public long countDevelopers() {
        return countNodes(NodeLabel.DEVELOPER);
    }

    public long countCompanies() {
        return countNodes(NodeLabel.COMPANY);
    }

    public long countSkills() {
        return countNodes(NodeLabel.SKILL);
    }

    public long countProjects() {
        return countNodes(NodeLabel.PROJECT);
    }

    public List<SkillUsage> findTopSkills(int limit) {
        return read(TOP_SKILLS, Map.of("limit", limit),
                record -> new SkillUsage(
                        GraphRecordMapper.toSkill(record.get("s").asNode()),
                        record.get("developerCount").asLong()
                ));
    }
}
