package com.devgraph.dto;

import java.util.List;

public record ConnectionDiscoveryResponse(
    String developerId,
    List<ConnectionRecord> connections,
    int offset,
    int limit,
    int resultCount) {
}
