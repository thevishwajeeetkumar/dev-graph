package com.devgraph.repository;

import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;

import com.devgraph.dto.PathNode;
import com.devgraph.model.Company;
import com.devgraph.model.Developer;
import com.devgraph.model.Industry;
import com.devgraph.model.Project;
import com.devgraph.model.Skill;

/**
 * Converts Neo4j driver {@link Node} results into DevGraph domain models.
 * Centralized here so every repository maps nodes the same way and missing
 * properties degrade to {@code null} instead of throwing.
 */
public final class GraphRecordMapper {

    private GraphRecordMapper() {
    }

    public static Developer toDeveloper(Node node) {
        return new Developer(str(node, "id"), str(node, "name"), str(node, "title"), str(node, "location"));
    }

    public static Company toCompany(Node node) {
        return new Company(str(node, "id"), str(node, "name"));
    }

    public static Project toProject(Node node) {
        return new Project(str(node, "id"), str(node, "name"), str(node, "description"));
    }

    public static Skill toSkill(Node node) {
        return new Skill(str(node, "id"), str(node, "name"));
    }

    public static Industry toIndustry(Node node) {
        return new Industry(str(node, "id"), str(node, "name"));
    }

    public static PathNode toPathNode(Node node) {
        String label = node.labels().iterator().hasNext() ? node.labels().iterator().next() : "Unknown";
        return new PathNode(label, str(node, "id"), str(node, "name"));
    }

    private static String str(Node node, String key) {
        Value value = node.get(key);
        return value == null || value.isNull() ? null : value.asString();
    }
}
