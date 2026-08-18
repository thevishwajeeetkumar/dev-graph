package com.devgraph.dto;

import java.util.List;

public record TalentBridgeResponse(
    String companyAId,
    String companyBId,
    List<TalentBridgeRecord> bridges,
    int offset,
    int limit,
    int resultCount) {
}
