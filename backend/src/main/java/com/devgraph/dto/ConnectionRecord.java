package com.devgraph.dto;

import java.util.List;

import com.devgraph.model.Company;
import com.devgraph.model.Developer;
import com.devgraph.model.Project;

/**
 * A single Developer -> Project -> Developer -> Company connection found
 * for the Professional Connection Discovery feature.
 */
public record ConnectionRecord(
    Developer colleague, 
    Project sharedProject, 
    List<Company> companies) {
}
