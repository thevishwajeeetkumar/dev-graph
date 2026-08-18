package com.devgraph.dto;

import java.util.List;

public record ShortestPathResponse(boolean pathFound, List<PathNode> nodes, List<String> relationshipTypes, int hops) {

    public static ShortestPathResponse notFound() {
        return new ShortestPathResponse(false, List.of(), List.of(), 0);
    }
}
