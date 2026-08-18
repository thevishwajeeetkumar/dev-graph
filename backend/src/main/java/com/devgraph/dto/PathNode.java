package com.devgraph.dto;

/**
 * A generic node in a traversed path, since a shortest-path result can mix
 * Developer and Company nodes.
 */
public record PathNode(
    String label,
    String id, 
    String name) {
}
