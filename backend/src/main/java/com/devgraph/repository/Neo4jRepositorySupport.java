package com.devgraph.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.neo4j.driver.exceptions.SessionExpiredException;
import org.neo4j.driver.exceptions.TransientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.devgraph.exception.GraphDatabaseQueryException;
import com.devgraph.exception.GraphDatabaseUnavailableException;

/**
 * Shared Cypher execution helpers built directly on the Neo4j driver, since
 * CognoDB is queried over Bolt without Spring Data Neo4j. Every session is
 * opened and closed via try-with-resources, and every driver-level failure
 * is translated into the {@code com.devgraph.exception} hierarchy here, at
 * the boundary, so nothing above this layer needs to know about
 * {@code org.neo4j.driver.exceptions}.
 */
public abstract class Neo4jRepositorySupport {

    private static final Logger log = LoggerFactory.getLogger(Neo4jRepositorySupport.class);

    @Autowired
    protected Driver driver;

    protected <T> List<T> read(String cypher, Map<String, Object> params, Function<Record, T> mapper) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(cypher, params);
                return result.list(mapper::apply);
            });
        } catch (ServiceUnavailableException | SessionExpiredException | TransientException ex) {
            log.error("CognoDB unavailable while executing read query", ex);
            throw new GraphDatabaseUnavailableException("The graph database is temporarily unavailable", ex);
        } catch (Neo4jException ex) {
            log.error("CognoDB reported an error while executing read query: {}", cypher, ex);
            throw new GraphDatabaseQueryException("The graph database reported an error", ex);
        }
    }

    protected <T> T write(String cypher, Map<String, Object> params, Function<Result, T> mapper) {
        try (Session session = driver.session()) {
            return session.executeWrite(tx -> mapper.apply(tx.run(cypher, params)));
        } catch (ServiceUnavailableException | SessionExpiredException | TransientException ex) {
            log.error("CognoDB unavailable while executing write query", ex);
            throw new GraphDatabaseUnavailableException("The graph database is temporarily unavailable", ex);
        } catch (Neo4jException ex) {
            log.error("CognoDB reported an error while executing write query: {}", cypher, ex);
            throw new GraphDatabaseQueryException("The graph database reported an error", ex);
        }
    }

    /**
     * Checks whether a node with the given label and business {@code id}
     * property exists. {@code label} is restricted to the {@link NodeLabel}
     * enum precisely so this can never be handed user-controlled input.
     */
    protected boolean exists(NodeLabel label, String id) {
        String cypher = "MATCH (n:" + label.cypherLabel() + " {id: $id}) RETURN count(n) > 0 AS found";
        return read(cypher, Map.of("id", id), record -> record.get("found").asBoolean())
                .stream()
                .findFirst()
                .orElse(false);
    }

    /**
     * Counts nodes of the given label. Same label-allow-list rationale as
     * {@link #exists}.
     */
    protected long countNodes(NodeLabel label) {
        String cypher = "MATCH (n:" + label.cypherLabel() + ") RETURN count(n) AS total";
        return read(cypher, Map.of(), record -> record.get("total").asLong()).stream().findFirst().orElse(0L);
    }

    /**
     * Counts relationships of the given type. {@code type} is restricted to
     * the {@link RelationshipType} enum for the same reason {@code label}
     * is restricted to {@link NodeLabel} in {@link #exists}.
     */
    protected long countRelationships(RelationshipType type) {
        String cypher = "MATCH ()-[r:" + type.name() + "]->() RETURN count(r) AS total";
        return read(cypher, Map.of(), record -> record.get("total").asLong()).stream().findFirst().orElse(0L);
    }
}
