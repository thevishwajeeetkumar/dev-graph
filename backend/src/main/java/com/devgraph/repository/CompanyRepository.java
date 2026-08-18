package com.devgraph.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.devgraph.model.Company;

@Repository
public class CompanyRepository extends Neo4jRepositorySupport {

    public List<Company> findAll() {
        return read(
                "MATCH (c:Company) RETURN c ORDER BY c.name",
                Map.of(),
                record -> GraphRecordMapper.toCompany(record.get("c").asNode())
        );
    }
}
