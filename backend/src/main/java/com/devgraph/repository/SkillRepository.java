package com.devgraph.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.devgraph.model.Skill;

@Repository
public class SkillRepository extends Neo4jRepositorySupport {

    public List<Skill> findAll() {
        return read(
                "MATCH (s:Skill) RETURN s ORDER BY s.name",
                Map.of(),
                record -> GraphRecordMapper.toSkill(record.get("s").asNode())
        );
    }
}
