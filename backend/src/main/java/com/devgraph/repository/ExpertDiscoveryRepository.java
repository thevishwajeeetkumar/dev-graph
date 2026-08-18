package com.devgraph.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.springframework.stereotype.Repository;

import com.devgraph.model.Skill;

/**
 * Feature 5: Nearest Expert Discovery
 * Developer -> [KNOWS ...] -> nearest colleague who holds a given skill
 *
 * <p>Unlike Shortest Path (fixed target id), the target here is not known in
 * advance - it's "whichever developer holding this skill is closest in the
 * KNOWS network". That's resolved in two steps instead of one Cypher query:
 * first collect the candidate skill holders, then run {@code shortestPath}
 * from the start developer to each candidate id and keep the shortest. Each
 * candidate is matched fresh by id inline in its own clause (never a
 * variable reused from an earlier-bound scope), the same rule the other
 * repositories follow to avoid CognoDB's bound-variable-in-later-pattern
 * quirk (see HiddenSkillRepository).</p>
 */
@Repository
public class ExpertDiscoveryRepository extends Neo4jRepositorySupport {

    private static final String FIND_SKILL = """
            MATCH (skill:Skill {id: $skillId})
            RETURN skill
            LIMIT 1
            """;

    private static final String FIND_DIRECT_HAS_SKILL = """
            MATCH (developer:Developer {id: $developerId})-[:HAS_SKILL]->(skill:Skill {id: $skillId})
            RETURN developer
            LIMIT 1
            """;

    private static final String FIND_SKILL_HOLDER_IDS = """
            MATCH (skill:Skill {id: $skillId})<-[:HAS_SKILL]-(expert:Developer)
            WHERE expert.id <> $developerId
            RETURN DISTINCT expert.id AS expertId
            """;

    /**
     * {@code maxHops} can't be parameterized (same restriction as the
     * variable-length bound in ShortestPathRepository), so it's interpolated
     * from a server-validated integer range, never user input directly.
     */
    private static final String FIND_NEAREST_EXPERT_PATH_TEMPLATE = """
            UNWIND $expertIds AS expertId
            MATCH path = shortestPath((start:Developer {id: $developerId})-[:KNOWS*1..%d]-(target:Developer {id: expertId}))
            RETURN path
            ORDER BY length(path) ASC
            LIMIT 1
            """;

    public boolean developerExists(String developerId) {
        return exists(NodeLabel.DEVELOPER, developerId);
    }

    public Skill findSkill(String skillId) {
        return read(FIND_SKILL, Map.of("skillId", skillId),
                record -> GraphRecordMapper.toSkill(record.get("skill").asNode()))
                .stream()
                .findFirst()
                .orElse(null);
    }

    public boolean developerHasSkillDirectly(String developerId, String skillId) {
        return !read(FIND_DIRECT_HAS_SKILL, Map.of("developerId", developerId, "skillId", skillId),
                record -> record.get("developer").asNode()).isEmpty();
    }

    public List<String> findSkillHolderIds(String developerId, String skillId) {
        return read(FIND_SKILL_HOLDER_IDS, Map.of("developerId", developerId, "skillId", skillId),
                record -> record.get("expertId").asString());
    }

    public NearestExpertPath findNearestExpertPath(String developerId, List<String> expertIds, int maxHops) {
        if (expertIds.isEmpty()) {
            return null;
        }
        String cypher = FIND_NEAREST_EXPERT_PATH_TEMPLATE.formatted(maxHops);
        List<NearestExpertPath> results = read(cypher,
                Map.of("developerId", developerId, "expertIds", expertIds),
                this::mapPath);
        return results.stream().findFirst().orElse(null);
    }

    private NearestExpertPath mapPath(org.neo4j.driver.Record record) {
        Path path = record.get("path").asPath();
        List<com.devgraph.dto.PathNode> nodes = new ArrayList<>();
        path.nodes().forEach(node -> nodes.add(GraphRecordMapper.toPathNode(node)));

        List<String> relationshipTypes = new ArrayList<>();
        for (Relationship relationship : path.relationships()) {
            relationshipTypes.add(relationship.type());
        }

        return new NearestExpertPath(nodes, relationshipTypes, path.length());
    }

    public record NearestExpertPath(List<com.devgraph.dto.PathNode> nodes, List<String> relationshipTypes, int hops) {
    }
}
