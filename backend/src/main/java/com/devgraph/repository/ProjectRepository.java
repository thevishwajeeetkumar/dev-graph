package com.devgraph.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.devgraph.model.Project;

@Repository
public class ProjectRepository extends Neo4jRepositorySupport {

    public List<Project> findAll() {
        return read(
                "MATCH (p:Project) RETURN p ORDER BY p.name",
                Map.of(),
                record -> GraphRecordMapper.toProject(record.get("p").asNode())
        );
    }

    public List<Project> findByDeveloperId(String developerId) {
        return read(
                "MATCH (:Developer {id: $developerId})-[:BUILT]->(p:Project) RETURN p ORDER BY p.name",
                Map.of("developerId", developerId),
                record -> GraphRecordMapper.toProject(record.get("p").asNode())
        );
    }

    public boolean developerExists(String developerId) {
        return exists(NodeLabel.DEVELOPER, developerId);
    }
}
