package com.devgraph.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.devgraph.model.Company;
import com.devgraph.model.Developer;
import com.devgraph.model.Skill;

@Repository
public class DeveloperRepository extends Neo4jRepositorySupport {

    public List<Developer> findAll() {
        return read(
                "MATCH (d:Developer) RETURN d ORDER BY d.name",
                Map.of(),
                record -> GraphRecordMapper.toDeveloper(record.get("d").asNode())
        );
    }

    public Optional<Developer> findById(String id) {
        return read(
                "MATCH (d:Developer {id: $id}) RETURN d",
                Map.of("id", id),
                record -> GraphRecordMapper.toDeveloper(record.get("d").asNode())
        ).stream().findFirst();
    }

    public boolean exists(String id) {
        return exists(NodeLabel.DEVELOPER, id);
    }

    public List<Company> findCompaniesByDeveloperId(String id) {
        return read(
                "MATCH (:Developer {id: $id})-[:WORKED_AT]->(c:Company) RETURN c ORDER BY c.name",
                Map.of("id", id),
                record -> GraphRecordMapper.toCompany(record.get("c").asNode())
        );
    }

    public List<Skill> findSkillsByDeveloperId(String id) {
        return read(
                "MATCH (:Developer {id: $id})-[:HAS_SKILL]->(s:Skill) RETURN s ORDER BY s.name",
                Map.of("id", id),
                record -> GraphRecordMapper.toSkill(record.get("s").asNode())
        );
    }
}
