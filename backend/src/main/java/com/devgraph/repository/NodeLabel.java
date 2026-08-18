package com.devgraph.repository;

/**
 * The fixed set of node labels in the DevGraph schema. Cypher cannot
 * parameterize labels, so {@link Neo4jRepositorySupport#exists} interpolates
 * one into the query string; restricting callers to this enum (instead of an
 * arbitrary String) guarantees only a known, compile-time-constant label
 * can ever reach that string, never anything derived from user input.
 */
public enum NodeLabel {
    DEVELOPER("Developer"),
    SKILL("Skill"),
    PROJECT("Project"),
    COMPANY("Company"),
    INDUSTRY("Industry");

    private final String cypherLabel;

    NodeLabel(String cypherLabel) {
        this.cypherLabel = cypherLabel;
    }

    public String cypherLabel() {
        return cypherLabel;
    }
}
