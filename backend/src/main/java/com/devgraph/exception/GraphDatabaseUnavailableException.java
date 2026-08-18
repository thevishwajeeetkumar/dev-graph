package com.devgraph.exception;

import org.springframework.http.HttpStatus;

/**
 * CognoDB is unreachable, dropped the session mid-query, or reported a
 * transient condition worth retrying. This is the client's cue to back off
 * and retry, not a bug in the request itself.
 */
public class GraphDatabaseUnavailableException extends GraphDatabaseException {
    public GraphDatabaseUnavailableException(String message, Throwable cause) {
        super(HttpStatus.SERVICE_UNAVAILABLE, message, cause);
    }
}
