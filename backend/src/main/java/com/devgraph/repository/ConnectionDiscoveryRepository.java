package com.devgraph.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.devgraph.dto.ConnectionRecord;

/**
 * Feature 1: Professional Connection Discovery
 * Developer -> Project -> Developer -> Company
 */
@Repository
public class ConnectionDiscoveryRepository extends Neo4jRepositorySupport {

    private static final String FIND_CONNECTIONS = """
            MATCH (source:Developer {id: $developerId})-[:BUILT]->(project:Project)<-[:BUILT]-(colleague:Developer)
            WHERE colleague.id <> $developerId
            OPTIONAL MATCH (colleague)-[:WORKED_AT]->(company:Company)
            WITH colleague, project, collect(DISTINCT company) AS companies
            RETURN colleague, project, companies
            ORDER BY colleague.name, project.name
            SKIP $offset
            LIMIT $limit
            """;

    public boolean developerExists(String developerId) {
        return exists(NodeLabel.DEVELOPER, developerId);
    }

    public List<ConnectionRecord> findConnections(String developerId, int offset, int limit) {
        return read(FIND_CONNECTIONS, Map.of("developerId", developerId, "offset", offset, "limit", limit), record -> new ConnectionRecord(
                GraphRecordMapper.toDeveloper(record.get("colleague").asNode()),
                GraphRecordMapper.toProject(record.get("project").asNode()),
                record.get("companies").asList(value -> GraphRecordMapper.toCompany(value.asNode()))
        ));
    }
}
