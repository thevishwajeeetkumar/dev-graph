package com.devgraph.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.devgraph.dto.TalentBridgeRecord;

/**
 * Feature 4: Talent Bridge Discovery
 * Company A -> Developer -> Developer -> Company B
 *
 * A bridge is either the same developer having worked at both companies
 * (DIRECT), or two developers who each worked at one of the companies and
 * KNOW each other (KNOWS).
 */
@Repository
public class TalentBridgeRepository extends Neo4jRepositorySupport {

    private static final String FIND_BRIDGES = """
            CALL {
              MATCH (developer:Developer)-[:WORKED_AT]->(:Company {id: $companyAId})
              MATCH (developer)-[:WORKED_AT]->(:Company {id: $companyBId})
              RETURN developer AS developerA, developer AS developerB, 'DIRECT' AS bridgeType
              UNION
              MATCH (:Company {id: $companyAId})<-[:WORKED_AT]-(developerA:Developer)-[:KNOWS]-(developerB:Developer)-[:WORKED_AT]->(:Company {id: $companyBId})
              WHERE developerA <> developerB
              RETURN DISTINCT developerA, developerB, 'KNOWS' AS bridgeType
            }
            RETURN developerA, developerB, bridgeType
            ORDER BY developerA.name, developerB.name
            SKIP $offset
            LIMIT $limit
            """;

    public boolean companyExists(String companyId) {
        return exists(NodeLabel.COMPANY, companyId);
    }

    public List<TalentBridgeRecord> findBridges(String companyAId, String companyBId, int offset, int limit) {
        return read(FIND_BRIDGES,
                Map.of("companyAId", companyAId, "companyBId", companyBId, "offset", offset, "limit", limit),
                record -> new TalentBridgeRecord(
                        GraphRecordMapper.toDeveloper(record.get("developerA").asNode()),
                        GraphRecordMapper.toDeveloper(record.get("developerB").asNode()),
                        record.get("bridgeType").asString()
                ));
    }
}
