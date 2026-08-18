package com.devgraph.exception;

import org.springframework.http.HttpStatus;

/**
 * CognoDB reported an error while processing an otherwise valid request
 * (malformed Cypher, a database-side constraint violation, an
 * authentication failure, etc). Reported as 502: the API itself is fine,
 * but its upstream dependency failed.
 */
public class GraphDatabaseQueryException extends GraphDatabaseException {
    public GraphDatabaseQueryException(String message, Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, message, cause);
    }
}
