package com.devgraph.repository;

/**
 * The fixed set of relationship types in the DevGraph schema. Same
 * rationale as {@link NodeLabel}: Cypher cannot parameterize a relationship
 * type, so restricting callers to this enum guarantees only a known,
 * compile-time-constant type can ever be interpolated into a query string.
 */
public enum RelationshipType {
    HAS_SKILL,
    WORKED_AT,
    BUILT,
    USES,
    IN_INDUSTRY,
    KNOWS
}
