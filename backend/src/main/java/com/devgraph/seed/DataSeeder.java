package com.devgraph.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.devgraph.repository.NodeLabel;
import com.devgraph.repository.RelationshipType;
import com.devgraph.repository.SeedRepository;

/**
 * Loads {@link SeedData} into CognoDB. Disabled by default
 * ({@code devgraph.seed.enabled=false}) - enable via env var
 * {@code DEVGRAPH_SEED_ENABLED=true} to run it. Every merge is idempotent
 * (Cypher {@code MERGE}, keyed on the same business {@code id}s every
 * time), so re-running against an already-seeded database is safe and just
 * re-applies the same data rather than duplicating it.
 *
 * Runs as a {@code CommandLineRunner} during startup - before the
 * {@code ApplicationReadyEvent}-triggered connectivity log line in
 * {@link com.devgraph.config.CognoDbConnectivityLogger} - so if CognoDB is
 * unreachable, seeding fails loudly with its own driver exception rather
 * than silently doing nothing.
 */
@Component
@Order(10)
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private SeedRepository seedRepository;

    @Value("${devgraph.seed.enabled:false}")
    private boolean seedEnabled;

    @Value("${devgraph.seed.reset:false}")
    private boolean resetBeforeSeed;

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            log.debug("Seeding disabled (devgraph.seed.enabled=false) - skipping");
            return;
        }

        log.info("Seeding CognoDB with sample DevGraph data...");
        long start = System.currentTimeMillis();

        if (resetBeforeSeed) {
            log.warn("devgraph.seed.reset=true - wiping ALL existing nodes and relationships first");
            seedRepository.wipeAll();
        }

        seedRepository.ensureConstraints();

        seedRepository.mergeCompanies(SeedData.COMPANIES);
        seedRepository.mergeIndustries(SeedData.INDUSTRIES);
        seedRepository.mergeSkills(SeedData.SKILLS);
        seedRepository.mergeDevelopers(SeedData.DEVELOPERS);
        seedRepository.mergeProjects(SeedData.PROJECTS);

        seedRepository.mergeCompanyIndustry(SeedData.COMPANY_INDUSTRY);
        seedRepository.mergeDeveloperSkills(SeedData.DEVELOPER_SKILLS);
        seedRepository.mergeDeveloperCompanies(SeedData.DEVELOPER_COMPANIES);
        seedRepository.mergeProjectBuilders(SeedData.PROJECT_BUILDERS);
        seedRepository.mergeProjectSkills(SeedData.PROJECT_SKILLS);
        seedRepository.mergeDeveloperKnows(SeedData.DEVELOPER_KNOWS);

        long elapsedMs = System.currentTimeMillis() - start;
        logSummary(elapsedMs);
    }

    private void logSummary(long elapsedMs) {
        log.info("Seeding complete in {} ms. Node counts: Developer={}, Skill={}, Project={}, Company={}, Industry={}",
                elapsedMs,
                seedRepository.countNodesOf(NodeLabel.DEVELOPER),
                seedRepository.countNodesOf(NodeLabel.SKILL),
                seedRepository.countNodesOf(NodeLabel.PROJECT),
                seedRepository.countNodesOf(NodeLabel.COMPANY),
                seedRepository.countNodesOf(NodeLabel.INDUSTRY));
        log.info("Relationship counts: HAS_SKILL={}, WORKED_AT={}, BUILT={}, USES={}, IN_INDUSTRY={}, KNOWS={}",
                seedRepository.countRelationshipsOf(RelationshipType.HAS_SKILL),
                seedRepository.countRelationshipsOf(RelationshipType.WORKED_AT),
                seedRepository.countRelationshipsOf(RelationshipType.BUILT),
                seedRepository.countRelationshipsOf(RelationshipType.USES),
                seedRepository.countRelationshipsOf(RelationshipType.IN_INDUSTRY),
                seedRepository.countRelationshipsOf(RelationshipType.KNOWS));
    }
}
