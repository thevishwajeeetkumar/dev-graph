package com.devgraph.seed;

import java.util.List;
import java.util.Map;

/**
 * Realistic sample data for local/demo CognoDB instances: a small but
 * densely-connected developer talent graph sized to exercise all four
 * power features, not just satisfy the schema.
 *
 * <ul>
 *   <li>{@code storefront}/{@code observability}/etc have multiple builders,
 *       so Connection Discovery has real Developer-Project-Developer fan-out.</li>
 *   <li>The KNOWS graph spans company boundaries with both short (2-hop) and
 *       longer chains, so Shortest Path has non-trivial routes to find -
 *       except Grace Lindqvist, deliberately left with zero KNOWS edges to
 *       demonstrate the "no path found" case.</li>
 *   <li>Several developers are missing 1-2 of the skills their own projects
 *       use (e.g. Ava Thompson built the Kafka-based Payment Gateway but
 *       doesn't have Kafka in HAS_SKILL), so Hidden Skill Discovery has
 *       real gaps to surface.</li>
 *   <li>Mia Nakamura (Shopify + Spotify) and Charlotte Dubois (Spotify +
 *       Netflix) each worked at two companies directly (a DIRECT bridge),
 *       while the KNOWS chain across companies provides KNOWS-type bridges
 *       too.</li>
 * </ul>
 */
public final class SeedData {

    private SeedData() {
    }

    public static final List<Map<String, Object>> INDUSTRIES = List.of(
            industry("ind-fintech", "Fintech"),
            industry("ind-entertainment", "Entertainment"),
            industry("ind-ecommerce", "E-commerce"),
            industry("ind-devtools", "Developer Tools"),
            industry("ind-travel", "Travel & Hospitality")
    );

    public static final List<Map<String, Object>> COMPANIES = List.of(
            company("co-stripe", "Stripe"),
            company("co-netflix", "Netflix"),
            company("co-spotify", "Spotify"),
            company("co-shopify", "Shopify"),
            company("co-datadog", "Datadog"),
            company("co-airbnb", "Airbnb")
    );

    public static final List<Map<String, Object>> COMPANY_INDUSTRY = List.of(
            companyIndustry("co-stripe", "ind-fintech"),
            companyIndustry("co-netflix", "ind-entertainment"),
            companyIndustry("co-spotify", "ind-entertainment"),
            companyIndustry("co-shopify", "ind-ecommerce"),
            companyIndustry("co-datadog", "ind-devtools"),
            companyIndustry("co-airbnb", "ind-travel")
    );

    public static final List<Map<String, Object>> SKILLS = List.of(
            skill("skill-java", "Java"),
            skill("skill-python", "Python"),
            skill("skill-javascript", "JavaScript"),
            skill("skill-typescript", "TypeScript"),
            skill("skill-react", "React"),
            skill("skill-nodejs", "Node.js"),
            skill("skill-go", "Go"),
            skill("skill-rust", "Rust"),
            skill("skill-kotlin", "Kotlin"),
            skill("skill-swift", "Swift"),
            skill("skill-kubernetes", "Kubernetes"),
            skill("skill-docker", "Docker"),
            skill("skill-aws", "AWS"),
            skill("skill-postgresql", "PostgreSQL"),
            skill("skill-mongodb", "MongoDB"),
            skill("skill-graphql", "GraphQL"),
            skill("skill-kafka", "Kafka"),
            skill("skill-redis", "Redis"),
            skill("skill-springboot", "Spring Boot"),
            skill("skill-prometheus", "Prometheus"),
            skill("skill-spark", "Apache Spark"),
            skill("skill-storybook", "Storybook")
    );

    public static final List<Map<String, Object>> DEVELOPERS = List.of(
            developer("dev-athompson", "Ava Thompson", "Senior Backend Engineer", "Seattle, WA"),
            developer("dev-lchen", "Liam Chen", "Frontend Engineer", "San Francisco, CA"),
            developer("dev-spatel", "Sophia Patel", "Full Stack Engineer", "Austin, TX"),
            developer("dev-nmartinez", "Noah Martinez", "DevOps Engineer", "Denver, CO"),
            developer("dev-ejohansson", "Emma Johansson", "Data Engineer", "Stockholm, Sweden"),
            developer("dev-okim", "Oliver Kim", "Backend Engineer", "Toronto, Canada"),
            developer("dev-irossi", "Isabella Rossi", "Mobile Engineer", "Milan, Italy"),
            developer("dev-ewright", "Ethan Wright", "Site Reliability Engineer", "London, UK"),
            developer("dev-mnakamura", "Mia Nakamura", "Frontend Engineer", "Tokyo, Japan"),
            developer("dev-lsilva", "Lucas Silva", "Full Stack Engineer", "Sao Paulo, Brazil"),
            developer("dev-anovak", "Amelia Novak", "Backend Engineer", "Berlin, Germany"),
            developer("dev-bosei", "Benjamin Osei", "Cloud Engineer", "Accra, Ghana"),
            developer("dev-cdubois", "Charlotte Dubois", "Data Scientist", "Paris, France"),
            developer("dev-dkowalski", "Daniel Kowalski", "Platform Engineer", "Warsaw, Poland"),
            developer("dev-glindqvist", "Grace Lindqvist", "Engineering Manager", "Stockholm, Sweden"),
            developer("dev-hadeyemi", "Henry Adeyemi", "Backend Engineer", "Lagos, Nigeria"),
            developer("dev-cfischer", "Chloe Fischer", "Frontend Engineer", "Munich, Germany"),
            developer("dev-roconnor", "Ryan O'Connor", "Infrastructure Engineer", "Dublin, Ireland")
    );

    public static final List<Map<String, Object>> DEVELOPER_SKILLS = List.of(
            developerSkill("dev-athompson", "skill-java"),
            developerSkill("dev-athompson", "skill-postgresql"),
            developerSkill("dev-athompson", "skill-springboot"),
            developerSkill("dev-athompson", "skill-aws"),
            developerSkill("dev-lchen", "skill-typescript"),
            developerSkill("dev-lchen", "skill-react"),
            developerSkill("dev-lchen", "skill-javascript"),
            developerSkill("dev-spatel", "skill-javascript"),
            developerSkill("dev-spatel", "skill-nodejs"),
            developerSkill("dev-spatel", "skill-react"),
            developerSkill("dev-nmartinez", "skill-go"),
            developerSkill("dev-nmartinez", "skill-docker"),
            developerSkill("dev-nmartinez", "skill-kubernetes"),
            developerSkill("dev-ejohansson", "skill-python"),
            developerSkill("dev-ejohansson", "skill-kafka"),
            developerSkill("dev-ejohansson", "skill-aws"),
            developerSkill("dev-okim", "skill-java"),
            developerSkill("dev-okim", "skill-postgresql"),
            developerSkill("dev-irossi", "skill-swift"),
            developerSkill("dev-irossi", "skill-kotlin"),
            developerSkill("dev-ewright", "skill-go"),
            developerSkill("dev-ewright", "skill-kubernetes"),
            developerSkill("dev-ewright", "skill-aws"),
            developerSkill("dev-mnakamura", "skill-typescript"),
            developerSkill("dev-mnakamura", "skill-react"),
            developerSkill("dev-mnakamura", "skill-javascript"),
            developerSkill("dev-lsilva", "skill-javascript"),
            developerSkill("dev-lsilva", "skill-nodejs"),
            developerSkill("dev-anovak", "skill-rust"),
            developerSkill("dev-anovak", "skill-postgresql"),
            developerSkill("dev-anovak", "skill-docker"),
            developerSkill("dev-bosei", "skill-go"),
            developerSkill("dev-bosei", "skill-aws"),
            developerSkill("dev-bosei", "skill-docker"),
            developerSkill("dev-cdubois", "skill-python"),
            developerSkill("dev-cdubois", "skill-postgresql"),
            developerSkill("dev-dkowalski", "skill-kubernetes"),
            developerSkill("dev-dkowalski", "skill-docker"),
            developerSkill("dev-dkowalski", "skill-go"),
            developerSkill("dev-glindqvist", "skill-java"),
            developerSkill("dev-glindqvist", "skill-python"),
            developerSkill("dev-glindqvist", "skill-aws"),
            developerSkill("dev-hadeyemi", "skill-python"),
            developerSkill("dev-hadeyemi", "skill-kafka"),
            developerSkill("dev-cfischer", "skill-typescript"),
            developerSkill("dev-cfischer", "skill-react"),
            developerSkill("dev-cfischer", "skill-javascript"),
            developerSkill("dev-cfischer", "skill-storybook"),
            developerSkill("dev-roconnor", "skill-go"),
            developerSkill("dev-roconnor", "skill-aws"),
            developerSkill("dev-roconnor", "skill-redis")
    );

    public static final List<Map<String, Object>> DEVELOPER_COMPANIES = List.of(
            developerCompany("dev-athompson", "co-stripe"),
            developerCompany("dev-okim", "co-stripe"),
            developerCompany("dev-glindqvist", "co-stripe"),
            developerCompany("dev-lchen", "co-shopify"),
            developerCompany("dev-spatel", "co-shopify"),
            developerCompany("dev-lsilva", "co-shopify"),
            developerCompany("dev-mnakamura", "co-shopify"),
            developerCompany("dev-mnakamura", "co-spotify"),
            developerCompany("dev-ejohansson", "co-spotify"),
            developerCompany("dev-cdubois", "co-spotify"),
            developerCompany("dev-cdubois", "co-netflix"),
            developerCompany("dev-hadeyemi", "co-netflix"),
            developerCompany("dev-cfischer", "co-netflix"),
            developerCompany("dev-nmartinez", "co-datadog"),
            developerCompany("dev-ewright", "co-datadog"),
            developerCompany("dev-dkowalski", "co-datadog"),
            developerCompany("dev-anovak", "co-datadog"),
            developerCompany("dev-irossi", "co-airbnb"),
            developerCompany("dev-bosei", "co-airbnb"),
            developerCompany("dev-roconnor", "co-airbnb")
    );

    public static final List<Map<String, Object>> PROJECTS = List.of(
            project("proj-payment-gateway", "Payment Gateway Revamp",
                    "Rebuilt the core payment processing pipeline for higher throughput and PCI compliance."),
            project("proj-reco-engine", "Realtime Recommendation Engine",
                    "Streaming recommendation service personalizing content in real time."),
            project("proj-storefront", "Storefront Redesign",
                    "Modernized the merchant storefront UI with a component-driven architecture."),
            project("proj-checkout-sdk", "Mobile Checkout SDK",
                    "Native mobile SDK for one-tap checkout across iOS and Android."),
            project("proj-observability", "Infra Observability Platform",
                    "Unified metrics, logs, and tracing platform for internal services."),
            project("proj-feature-flags", "Internal Feature Flag Service",
                    "Low-latency feature flag evaluation service used across the backend."),
            project("proj-cdn-edge", "Global CDN Edge Cache",
                    "Edge caching layer to cut origin load and latency worldwide."),
            project("proj-data-warehouse", "Data Warehouse Pipeline",
                    "Batch ETL pipeline feeding the analytics data warehouse."),
            project("proj-design-system", "Design System Component Library",
                    "Shared component library powering all first-party web apps."),
            project("proj-onboarding", "Onboarding Workflow Automation",
                    "Automated new-seller onboarding and document verification.")
    );

    public static final List<Map<String, Object>> PROJECT_BUILDERS = List.of(
            projectBuilder("dev-athompson", "proj-payment-gateway"),
            projectBuilder("dev-okim", "proj-payment-gateway"),
            projectBuilder("dev-ejohansson", "proj-reco-engine"),
            projectBuilder("dev-cdubois", "proj-reco-engine"),
            projectBuilder("dev-lchen", "proj-storefront"),
            projectBuilder("dev-mnakamura", "proj-storefront"),
            projectBuilder("dev-irossi", "proj-checkout-sdk"),
            projectBuilder("dev-spatel", "proj-checkout-sdk"),
            projectBuilder("dev-nmartinez", "proj-observability"),
            projectBuilder("dev-ewright", "proj-observability"),
            projectBuilder("dev-dkowalski", "proj-observability"),
            projectBuilder("dev-anovak", "proj-feature-flags"),
            projectBuilder("dev-bosei", "proj-cdn-edge"),
            projectBuilder("dev-roconnor", "proj-cdn-edge"),
            projectBuilder("dev-cdubois", "proj-data-warehouse"),
            projectBuilder("dev-hadeyemi", "proj-data-warehouse"),
            projectBuilder("dev-cfischer", "proj-design-system"),
            projectBuilder("dev-mnakamura", "proj-design-system"),
            projectBuilder("dev-spatel", "proj-onboarding"),
            projectBuilder("dev-lsilva", "proj-onboarding")
    );

    public static final List<Map<String, Object>> PROJECT_SKILLS = List.of(
            projectSkill("proj-payment-gateway", "skill-java"),
            projectSkill("proj-payment-gateway", "skill-postgresql"),
            projectSkill("proj-payment-gateway", "skill-kafka"),
            projectSkill("proj-payment-gateway", "skill-aws"),
            projectSkill("proj-reco-engine", "skill-python"),
            projectSkill("proj-reco-engine", "skill-kafka"),
            projectSkill("proj-reco-engine", "skill-redis"),
            projectSkill("proj-reco-engine", "skill-aws"),
            projectSkill("proj-storefront", "skill-typescript"),
            projectSkill("proj-storefront", "skill-react"),
            projectSkill("proj-storefront", "skill-graphql"),
            projectSkill("proj-checkout-sdk", "skill-kotlin"),
            projectSkill("proj-checkout-sdk", "skill-swift"),
            projectSkill("proj-observability", "skill-go"),
            projectSkill("proj-observability", "skill-kubernetes"),
            projectSkill("proj-observability", "skill-docker"),
            projectSkill("proj-observability", "skill-prometheus"),
            projectSkill("proj-feature-flags", "skill-rust"),
            projectSkill("proj-feature-flags", "skill-postgresql"),
            projectSkill("proj-cdn-edge", "skill-go"),
            projectSkill("proj-cdn-edge", "skill-redis"),
            projectSkill("proj-cdn-edge", "skill-aws"),
            projectSkill("proj-data-warehouse", "skill-python"),
            projectSkill("proj-data-warehouse", "skill-spark"),
            projectSkill("proj-data-warehouse", "skill-kafka"),
            projectSkill("proj-data-warehouse", "skill-postgresql"),
            projectSkill("proj-design-system", "skill-typescript"),
            projectSkill("proj-design-system", "skill-react"),
            projectSkill("proj-design-system", "skill-storybook"),
            projectSkill("proj-onboarding", "skill-nodejs"),
            projectSkill("proj-onboarding", "skill-mongodb"),
            projectSkill("proj-onboarding", "skill-graphql")
    );

    /** One direction per pair - KNOWS is queried as an undirected pattern. */
    public static final List<Map<String, Object>> DEVELOPER_KNOWS = List.of(
            developerKnows("dev-athompson", "dev-okim"),
            developerKnows("dev-okim", "dev-lchen"),
            developerKnows("dev-lchen", "dev-spatel"),
            developerKnows("dev-spatel", "dev-mnakamura"),
            developerKnows("dev-mnakamura", "dev-ejohansson"),
            developerKnows("dev-ejohansson", "dev-cdubois"),
            developerKnows("dev-cdubois", "dev-hadeyemi"),
            developerKnows("dev-hadeyemi", "dev-cfischer"),
            developerKnows("dev-nmartinez", "dev-ewright"),
            developerKnows("dev-ewright", "dev-dkowalski"),
            developerKnows("dev-dkowalski", "dev-anovak"),
            developerKnows("dev-irossi", "dev-bosei"),
            developerKnows("dev-bosei", "dev-roconnor"),
            developerKnows("dev-lchen", "dev-nmartinez"),
            developerKnows("dev-roconnor", "dev-athompson"),
            developerKnows("dev-lsilva", "dev-spatel"),
            developerKnows("dev-athompson", "dev-cfischer")
            // dev-glindqvist intentionally has no KNOWS edges - demonstrates
            // the "no path found" case for shortest-path queries.
    );

    private static Map<String, Object> industry(String id, String name) {
        return Map.of("id", id, "name", name);
    }

    private static Map<String, Object> company(String id, String name) {
        return Map.of("id", id, "name", name);
    }

    private static Map<String, Object> companyIndustry(String companyId, String industryId) {
        return Map.of("companyId", companyId, "industryId", industryId);
    }

    private static Map<String, Object> skill(String id, String name) {
        return Map.of("id", id, "name", name);
    }

    private static Map<String, Object> developer(String id, String name, String title, String location) {
        return Map.of("id", id, "name", name, "title", title, "location", location);
    }

    private static Map<String, Object> developerSkill(String developerId, String skillId) {
        return Map.of("developerId", developerId, "skillId", skillId);
    }

    private static Map<String, Object> developerCompany(String developerId, String companyId) {
        return Map.of("developerId", developerId, "companyId", companyId);
    }

    private static Map<String, Object> project(String id, String name, String description) {
        return Map.of("id", id, "name", name, "description", description);
    }

    private static Map<String, Object> projectBuilder(String developerId, String projectId) {
        return Map.of("developerId", developerId, "projectId", projectId);
    }

    private static Map<String, Object> projectSkill(String projectId, String skillId) {
        return Map.of("projectId", projectId, "skillId", skillId);
    }

    private static Map<String, Object> developerKnows(String developerAId, String developerBId) {
        return Map.of("developerAId", developerAId, "developerBId", developerBId);
    }
}
