package com.devgraph.dto;

import com.devgraph.model.Developer;

/**
 * A bridge between two companies. {@code bridgeType} is {@code DIRECT} when
 * the same developer worked at both companies, or {@code KNOWS} when two
 * different developers who each worked at one company know each other.
 */
public record TalentBridgeRecord(
    Developer developerA, 
    Developer developerB, 
    String bridgeType) {
}
