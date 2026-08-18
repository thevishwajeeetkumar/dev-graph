package com.devgraph.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.springframework.stereotype.Repository;

import com.devgraph.dto.PathNode;
import com.devgraph.dto.ShortestPathResponse;

/**
 * Feature 2: Shortest Connection Path
 * Developer -> Developer -> Developer -> Company
 *
 * <p>Split into two independent queries rather than one query using a
 * {@code KNOWS*0..N} zero-length variable relationship to cover "developer
 * already works at the target company" as hop 0. CognoDB's engine doesn't
 * handle that zero-length case correctly (verified against real data: a
 * developer with a direct WORKED_AT edge to the target came back as no
 * path found). Checking the direct case as its own plain MATCH, and the
 * multi-hop case as its own {@code KNOWS*1..N} shortestPath, sidesteps it -
 * both queries only ever bind {@code start}/{@code target} by id inline in
 * a single MATCH clause, never reusing a variable bound in an earlier
 * clause (see HiddenSkillRepository's javadoc for why that reuse pattern is
 * unreliable here).</p>
 */
@Repository
public class ShortestPathRepository extends Neo4jRepositorySupport {

    private static final int MIN_ALLOWED_HOPS = 1;
    private static final int MAX_ALLOWED_HOPS = 10;

    private static final String FIND_DIRECT_WORKED_AT = """
            MATCH (start:Developer {id: $developerId})-[:WORKED_AT]->(target:Company {id: $companyId})
            RETURN start, target
            LIMIT 1
            """;

    /**
     * Cypher cannot parameterize the {@code *min..max} bound of a
     * variable-length relationship, so {@code maxHops} has to be
     * interpolated into the query text. These bounds are enforced here too
     * - not just in the service layer - so this repository can never be
     * called, directly or indirectly, with a value large enough to make the
     * traversal expensive.
     */
    private static final String FIND_KNOWS_CHAIN_PATH_TEMPLATE = """
            MATCH path = shortestPath((start:Developer {id: $developerId})-[:KNOWS*1..%d]-(:Developer)-[:WORKED_AT]->(target:Company {id: $companyId}))
            RETURN path
            LIMIT 1
            """;

    public boolean developerExists(String developerId) {
        return exists(NodeLabel.DEVELOPER, developerId);
    }

    public boolean companyExists(String companyId) {
        return exists(NodeLabel.COMPANY, companyId);
    }

    public ShortestPathResponse findShortestPath(String developerId, String companyId, int maxHops) {
        if (maxHops < MIN_ALLOWED_HOPS || maxHops > MAX_ALLOWED_HOPS) {
            throw new IllegalArgumentException(
                    "maxHops must be between %d and %d".formatted(MIN_ALLOWED_HOPS, MAX_ALLOWED_HOPS));
        }

        ShortestPathResponse direct = findDirect(developerId, companyId);
        if (direct.pathFound()) {
            return direct;
        }

        String cypher = FIND_KNOWS_CHAIN_PATH_TEMPLATE.formatted(maxHops);
        List<ShortestPathResponse> results = read(cypher, Map.of("developerId", developerId, "companyId", companyId),
                record -> mapPath(record));
        return results.stream().findFirst().orElseGet(ShortestPathResponse::notFound);
    }

    private ShortestPathResponse findDirect(String developerId, String companyId) {
        List<ShortestPathResponse> results = read(FIND_DIRECT_WORKED_AT,
                Map.of("developerId", developerId, "companyId", companyId),
                record -> new ShortestPathResponse(
                        true,
                        List.of(
                                GraphRecordMapper.toPathNode(record.get("start").asNode()),
                                GraphRecordMapper.toPathNode(record.get("target").asNode())
                        ),
                        List.of("WORKED_AT"),
                        1
                ));
        return results.stream().findFirst().orElseGet(ShortestPathResponse::notFound);
    }

    private ShortestPathResponse mapPath(Record record) {
        var pathValue = record.get("path");
        if (pathValue == null || pathValue.isNull()) {
            return ShortestPathResponse.notFound();
        }
        Path path = pathValue.asPath();
        List<PathNode> nodes = new ArrayList<>();
        path.nodes().forEach(node -> nodes.add(GraphRecordMapper.toPathNode(node)));

        List<String> relationshipTypes = new ArrayList<>();
        for (Relationship relationship : path.relationships()) {
            relationshipTypes.add(relationship.type());
        }

        return new ShortestPathResponse(true, nodes, relationshipTypes, path.length());
    }
}
