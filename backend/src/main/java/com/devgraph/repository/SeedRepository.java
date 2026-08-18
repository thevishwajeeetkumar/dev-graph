package com.devgraph.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * Bulk-loading Cypher for {@code seed.DataSeeder}. Every node/relationship
 * query is a single UNWIND-over-MERGE statement (idempotent - safe to
 * re-run) rather than one round trip per row, and every list of rows is
 * bound as a single {@code $rows} parameter, never string-concatenated.
 */
@Repository
public class SeedRepository extends Neo4jRepositorySupport {

    private static final List<String> CONSTRAINTS = List.of(
            "CREATE CONSTRAINT devgraph_developer_id IF NOT EXISTS FOR (d:Developer) REQUIRE d.id IS UNIQUE",
            "CREATE CONSTRAINT devgraph_skill_id IF NOT EXISTS FOR (s:Skill) REQUIRE s.id IS UNIQUE",
            "CREATE CONSTRAINT devgraph_project_id IF NOT EXISTS FOR (p:Project) REQUIRE p.id IS UNIQUE",
            "CREATE CONSTRAINT devgraph_company_id IF NOT EXISTS FOR (c:Company) REQUIRE c.id IS UNIQUE",
            "CREATE CONSTRAINT devgraph_industry_id IF NOT EXISTS FOR (i:Industry) REQUIRE i.id IS UNIQUE"
    );

    public void ensureConstraints() {
        for (String cypher : CONSTRAINTS) {
            write(cypher, Map.of(), result -> {
                result.consume();
                return null;
            });
        }
    }

    /** DANGER: deletes every node and relationship in the graph. */
    public void wipeAll() {
        write("MATCH (n) DETACH DELETE n", Map.of(), result -> {
            result.consume();
            return null;
        });
    }

    public int mergeCompanies(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MERGE (c:Company {id: row.id})
                SET c.name = row.name
                """, rows);
    }

    public int mergeIndustries(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MERGE (i:Industry {id: row.id})
                SET i.name = row.name
                """, rows);
    }

    public int mergeCompanyIndustry(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MATCH (c:Company {id: row.companyId})
                MATCH (i:Industry {id: row.industryId})
                MERGE (c)-[:IN_INDUSTRY]->(i)
                """, rows);
    }

    public int mergeSkills(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MERGE (s:Skill {id: row.id})
                SET s.name = row.name
                """, rows);
    }

    public int mergeDevelopers(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MERGE (d:Developer {id: row.id})
                SET d.name = row.name, d.title = row.title, d.location = row.location
                """, rows);
    }

    public int mergeDeveloperSkills(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MATCH (d:Developer {id: row.developerId})
                MATCH (s:Skill {id: row.skillId})
                MERGE (d)-[:HAS_SKILL]->(s)
                """, rows);
    }

    public int mergeDeveloperCompanies(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MATCH (d:Developer {id: row.developerId})
                MATCH (c:Company {id: row.companyId})
                MERGE (d)-[:WORKED_AT]->(c)
                """, rows);
    }

    public int mergeProjects(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MERGE (p:Project {id: row.id})
                SET p.name = row.name, p.description = row.description
                """, rows);
    }

    public int mergeProjectBuilders(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MATCH (d:Developer {id: row.developerId})
                MATCH (p:Project {id: row.projectId})
                MERGE (d)-[:BUILT]->(p)
                """, rows);
    }

    public int mergeProjectSkills(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MATCH (p:Project {id: row.projectId})
                MATCH (s:Skill {id: row.skillId})
                MERGE (p)-[:USES]->(s)
                """, rows);
    }

    public int mergeDeveloperKnows(List<Map<String, Object>> rows) {
        return mergeRows("""
                UNWIND $rows AS row
                MATCH (a:Developer {id: row.developerAId})
                MATCH (b:Developer {id: row.developerBId})
                MERGE (a)-[:KNOWS]->(b)
                """, rows);
    }

    public long countNodesOf(NodeLabel label) {
        return countNodes(label);
    }

    public long countRelationshipsOf(RelationshipType type) {
        return countRelationships(type);
    }

    private int mergeRows(String cypher, List<Map<String, Object>> rows) {
        return write(cypher, Map.of("rows", rows), result -> {
            result.consume();
            return rows.size();
        });
    }
}
