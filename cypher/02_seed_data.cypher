// DevGraph — sample data load
// Mirrors backend/src/main/java/com/devgraph/seed/SeedData.java exactly, so
// running this file (via cypher-shell / Neo4j Browser / CognoDB console)
// produces the same graph the application seeds itself with on startup.
//
// Every block is UNWIND-over-MERGE, so the whole file is idempotent and
// safe to re-run. Run 01_constraints.cypher first.

// ---------------------------------------------------------------------
// Industries
// ---------------------------------------------------------------------
UNWIND [
  {id:'ind-fintech',       name:'Fintech'},
  {id:'ind-entertainment', name:'Entertainment'},
  {id:'ind-ecommerce',     name:'E-commerce'},
  {id:'ind-devtools',      name:'Developer Tools'},
  {id:'ind-travel',        name:'Travel & Hospitality'}
] AS row
MERGE (i:Industry {id: row.id})
SET i.name = row.name;

// ---------------------------------------------------------------------
// Companies
// ---------------------------------------------------------------------
UNWIND [
  {id:'co-stripe',   name:'Stripe'},
  {id:'co-netflix',  name:'Netflix'},
  {id:'co-spotify',  name:'Spotify'},
  {id:'co-shopify',  name:'Shopify'},
  {id:'co-datadog',  name:'Datadog'},
  {id:'co-airbnb',   name:'Airbnb'}
] AS row
MERGE (c:Company {id: row.id})
SET c.name = row.name;

// Company -[:IN_INDUSTRY]-> Industry
UNWIND [
  {companyId:'co-stripe',  industryId:'ind-fintech'},
  {companyId:'co-netflix', industryId:'ind-entertainment'},
  {companyId:'co-spotify', industryId:'ind-entertainment'},
  {companyId:'co-shopify', industryId:'ind-ecommerce'},
  {companyId:'co-datadog', industryId:'ind-devtools'},
  {companyId:'co-airbnb',  industryId:'ind-travel'}
] AS row
MATCH (c:Company {id: row.companyId})
MATCH (i:Industry {id: row.industryId})
MERGE (c)-[:IN_INDUSTRY]->(i);

// ---------------------------------------------------------------------
// Skills
// ---------------------------------------------------------------------
UNWIND [
  {id:'skill-java',       name:'Java'},
  {id:'skill-python',     name:'Python'},
  {id:'skill-javascript', name:'JavaScript'},
  {id:'skill-typescript', name:'TypeScript'},
  {id:'skill-react',      name:'React'},
  {id:'skill-nodejs',     name:'Node.js'},
  {id:'skill-go',         name:'Go'},
  {id:'skill-rust',       name:'Rust'},
  {id:'skill-kotlin',     name:'Kotlin'},
  {id:'skill-swift',      name:'Swift'},
  {id:'skill-kubernetes', name:'Kubernetes'},
  {id:'skill-docker',     name:'Docker'},
  {id:'skill-aws',        name:'AWS'},
  {id:'skill-postgresql', name:'PostgreSQL'},
  {id:'skill-mongodb',    name:'MongoDB'},
  {id:'skill-graphql',    name:'GraphQL'},
  {id:'skill-kafka',      name:'Kafka'},
  {id:'skill-redis',      name:'Redis'},
  {id:'skill-springboot', name:'Spring Boot'},
  {id:'skill-prometheus', name:'Prometheus'},
  {id:'skill-spark',      name:'Apache Spark'},
  {id:'skill-storybook',  name:'Storybook'}
] AS row
MERGE (s:Skill {id: row.id})
SET s.name = row.name;

// ---------------------------------------------------------------------
// Developers
// ---------------------------------------------------------------------
UNWIND [
  {id:'dev-athompson',  name:'Ava Thompson',      title:'Senior Backend Engineer', location:'Seattle, WA'},
  {id:'dev-lchen',      name:'Liam Chen',         title:'Frontend Engineer',       location:'San Francisco, CA'},
  {id:'dev-spatel',     name:'Sophia Patel',      title:'Full Stack Engineer',     location:'Austin, TX'},
  {id:'dev-nmartinez',  name:'Noah Martinez',     title:'DevOps Engineer',         location:'Denver, CO'},
  {id:'dev-ejohansson', name:'Emma Johansson',    title:'Data Engineer',           location:'Stockholm, Sweden'},
  {id:'dev-okim',       name:'Oliver Kim',        title:'Backend Engineer',        location:'Toronto, Canada'},
  {id:'dev-irossi',     name:'Isabella Rossi',    title:'Mobile Engineer',         location:'Milan, Italy'},
  {id:'dev-ewright',    name:'Ethan Wright',      title:'Site Reliability Engineer', location:'London, UK'},
  {id:'dev-mnakamura',  name:'Mia Nakamura',      title:'Frontend Engineer',       location:'Tokyo, Japan'},
  {id:'dev-lsilva',     name:'Lucas Silva',       title:'Full Stack Engineer',     location:'Sao Paulo, Brazil'},
  {id:'dev-anovak',     name:'Amelia Novak',      title:'Backend Engineer',        location:'Berlin, Germany'},
  {id:'dev-bosei',      name:'Benjamin Osei',     title:'Cloud Engineer',          location:'Accra, Ghana'},
  {id:'dev-cdubois',    name:'Charlotte Dubois',  title:'Data Scientist',          location:'Paris, France'},
  {id:'dev-dkowalski',  name:'Daniel Kowalski',   title:'Platform Engineer',       location:'Warsaw, Poland'},
  {id:'dev-glindqvist', name:'Grace Lindqvist',   title:'Engineering Manager',     location:'Stockholm, Sweden'},
  {id:'dev-hadeyemi',   name:'Henry Adeyemi',     title:'Backend Engineer',        location:'Lagos, Nigeria'},
  {id:'dev-cfischer',   name:'Chloe Fischer',     title:'Frontend Engineer',       location:'Munich, Germany'},
  {id:'dev-roconnor',   name:"Ryan O'Connor",     title:'Infrastructure Engineer', location:'Dublin, Ireland'}
] AS row
MERGE (d:Developer {id: row.id})
SET d.name = row.name, d.title = row.title, d.location = row.location;

// Developer -[:HAS_SKILL]-> Skill
UNWIND [
  {developerId:'dev-athompson',  skillId:'skill-java'},
  {developerId:'dev-athompson',  skillId:'skill-postgresql'},
  {developerId:'dev-athompson',  skillId:'skill-springboot'},
  {developerId:'dev-athompson',  skillId:'skill-aws'},
  {developerId:'dev-lchen',      skillId:'skill-typescript'},
  {developerId:'dev-lchen',      skillId:'skill-react'},
  {developerId:'dev-lchen',      skillId:'skill-javascript'},
  {developerId:'dev-spatel',     skillId:'skill-javascript'},
  {developerId:'dev-spatel',     skillId:'skill-nodejs'},
  {developerId:'dev-spatel',     skillId:'skill-react'},
  {developerId:'dev-nmartinez',  skillId:'skill-go'},
  {developerId:'dev-nmartinez',  skillId:'skill-docker'},
  {developerId:'dev-nmartinez',  skillId:'skill-kubernetes'},
  {developerId:'dev-ejohansson', skillId:'skill-python'},
  {developerId:'dev-ejohansson', skillId:'skill-kafka'},
  {developerId:'dev-ejohansson', skillId:'skill-aws'},
  {developerId:'dev-okim',       skillId:'skill-java'},
  {developerId:'dev-okim',       skillId:'skill-postgresql'},
  {developerId:'dev-irossi',     skillId:'skill-swift'},
  {developerId:'dev-irossi',     skillId:'skill-kotlin'},
  {developerId:'dev-ewright',    skillId:'skill-go'},
  {developerId:'dev-ewright',    skillId:'skill-kubernetes'},
  {developerId:'dev-ewright',    skillId:'skill-aws'},
  {developerId:'dev-mnakamura',  skillId:'skill-typescript'},
  {developerId:'dev-mnakamura',  skillId:'skill-react'},
  {developerId:'dev-mnakamura',  skillId:'skill-javascript'},
  {developerId:'dev-lsilva',     skillId:'skill-javascript'},
  {developerId:'dev-lsilva',     skillId:'skill-nodejs'},
  {developerId:'dev-anovak',     skillId:'skill-rust'},
  {developerId:'dev-anovak',     skillId:'skill-postgresql'},
  {developerId:'dev-anovak',     skillId:'skill-docker'},
  {developerId:'dev-bosei',      skillId:'skill-go'},
  {developerId:'dev-bosei',      skillId:'skill-aws'},
  {developerId:'dev-bosei',      skillId:'skill-docker'},
  {developerId:'dev-cdubois',    skillId:'skill-python'},
  {developerId:'dev-cdubois',    skillId:'skill-postgresql'},
  {developerId:'dev-dkowalski',  skillId:'skill-kubernetes'},
  {developerId:'dev-dkowalski',  skillId:'skill-docker'},
  {developerId:'dev-dkowalski',  skillId:'skill-go'},
  {developerId:'dev-glindqvist', skillId:'skill-java'},
  {developerId:'dev-glindqvist', skillId:'skill-python'},
  {developerId:'dev-glindqvist', skillId:'skill-aws'},
  {developerId:'dev-hadeyemi',   skillId:'skill-python'},
  {developerId:'dev-hadeyemi',   skillId:'skill-kafka'},
  {developerId:'dev-cfischer',   skillId:'skill-typescript'},
  {developerId:'dev-cfischer',   skillId:'skill-react'},
  {developerId:'dev-cfischer',   skillId:'skill-javascript'},
  {developerId:'dev-cfischer',   skillId:'skill-storybook'},
  {developerId:'dev-roconnor',   skillId:'skill-go'},
  {developerId:'dev-roconnor',   skillId:'skill-aws'},
  {developerId:'dev-roconnor',   skillId:'skill-redis'}
] AS row
MATCH (d:Developer {id: row.developerId})
MATCH (s:Skill {id: row.skillId})
MERGE (d)-[:HAS_SKILL]->(s);

// Developer -[:WORKED_AT]-> Company
UNWIND [
  {developerId:'dev-athompson',  companyId:'co-stripe'},
  {developerId:'dev-okim',       companyId:'co-stripe'},
  {developerId:'dev-glindqvist', companyId:'co-stripe'},
  {developerId:'dev-lchen',      companyId:'co-shopify'},
  {developerId:'dev-spatel',     companyId:'co-shopify'},
  {developerId:'dev-lsilva',     companyId:'co-shopify'},
  {developerId:'dev-mnakamura',  companyId:'co-shopify'},
  {developerId:'dev-mnakamura',  companyId:'co-spotify'},
  {developerId:'dev-ejohansson', companyId:'co-spotify'},
  {developerId:'dev-cdubois',    companyId:'co-spotify'},
  {developerId:'dev-cdubois',    companyId:'co-netflix'},
  {developerId:'dev-hadeyemi',   companyId:'co-netflix'},
  {developerId:'dev-cfischer',   companyId:'co-netflix'},
  {developerId:'dev-nmartinez',  companyId:'co-datadog'},
  {developerId:'dev-ewright',    companyId:'co-datadog'},
  {developerId:'dev-dkowalski',  companyId:'co-datadog'},
  {developerId:'dev-anovak',     companyId:'co-datadog'},
  {developerId:'dev-irossi',     companyId:'co-airbnb'},
  {developerId:'dev-bosei',      companyId:'co-airbnb'},
  {developerId:'dev-roconnor',   companyId:'co-airbnb'}
] AS row
MATCH (d:Developer {id: row.developerId})
MATCH (c:Company {id: row.companyId})
MERGE (d)-[:WORKED_AT]->(c);

// ---------------------------------------------------------------------
// Projects
// ---------------------------------------------------------------------
UNWIND [
  {id:'proj-payment-gateway', name:'Payment Gateway Revamp',
   description:'Rebuilt the core payment processing pipeline for higher throughput and PCI compliance.'},
  {id:'proj-reco-engine', name:'Realtime Recommendation Engine',
   description:'Streaming recommendation service personalizing content in real time.'},
  {id:'proj-storefront', name:'Storefront Redesign',
   description:'Modernized the merchant storefront UI with a component-driven architecture.'},
  {id:'proj-checkout-sdk', name:'Mobile Checkout SDK',
   description:'Native mobile SDK for one-tap checkout across iOS and Android.'},
  {id:'proj-observability', name:'Infra Observability Platform',
   description:'Unified metrics, logs, and tracing platform for internal services.'},
  {id:'proj-feature-flags', name:'Internal Feature Flag Service',
   description:'Low-latency feature flag evaluation service used across the backend.'},
  {id:'proj-cdn-edge', name:'Global CDN Edge Cache',
   description:'Edge caching layer to cut origin load and latency worldwide.'},
  {id:'proj-data-warehouse', name:'Data Warehouse Pipeline',
   description:'Batch ETL pipeline feeding the analytics data warehouse.'},
  {id:'proj-design-system', name:'Design System Component Library',
   description:'Shared component library powering all first-party web apps.'},
  {id:'proj-onboarding', name:'Onboarding Workflow Automation',
   description:'Automated new-seller onboarding and document verification.'}
] AS row
MERGE (p:Project {id: row.id})
SET p.name = row.name, p.description = row.description;

// Developer -[:BUILT]-> Project
UNWIND [
  {developerId:'dev-athompson',  projectId:'proj-payment-gateway'},
  {developerId:'dev-okim',       projectId:'proj-payment-gateway'},
  {developerId:'dev-ejohansson', projectId:'proj-reco-engine'},
  {developerId:'dev-cdubois',    projectId:'proj-reco-engine'},
  {developerId:'dev-lchen',      projectId:'proj-storefront'},
  {developerId:'dev-mnakamura',  projectId:'proj-storefront'},
  {developerId:'dev-irossi',     projectId:'proj-checkout-sdk'},
  {developerId:'dev-spatel',     projectId:'proj-checkout-sdk'},
  {developerId:'dev-nmartinez',  projectId:'proj-observability'},
  {developerId:'dev-ewright',    projectId:'proj-observability'},
  {developerId:'dev-dkowalski',  projectId:'proj-observability'},
  {developerId:'dev-anovak',     projectId:'proj-feature-flags'},
  {developerId:'dev-bosei',      projectId:'proj-cdn-edge'},
  {developerId:'dev-roconnor',   projectId:'proj-cdn-edge'},
  {developerId:'dev-cdubois',    projectId:'proj-data-warehouse'},
  {developerId:'dev-hadeyemi',   projectId:'proj-data-warehouse'},
  {developerId:'dev-cfischer',   projectId:'proj-design-system'},
  {developerId:'dev-mnakamura',  projectId:'proj-design-system'},
  {developerId:'dev-spatel',     projectId:'proj-onboarding'},
  {developerId:'dev-lsilva',     projectId:'proj-onboarding'}
] AS row
MATCH (d:Developer {id: row.developerId})
MATCH (p:Project {id: row.projectId})
MERGE (d)-[:BUILT]->(p);

// Project -[:USES]-> Skill
UNWIND [
  {projectId:'proj-payment-gateway', skillId:'skill-java'},
  {projectId:'proj-payment-gateway', skillId:'skill-postgresql'},
  {projectId:'proj-payment-gateway', skillId:'skill-kafka'},
  {projectId:'proj-payment-gateway', skillId:'skill-aws'},
  {projectId:'proj-reco-engine',     skillId:'skill-python'},
  {projectId:'proj-reco-engine',     skillId:'skill-kafka'},
  {projectId:'proj-reco-engine',     skillId:'skill-redis'},
  {projectId:'proj-reco-engine',     skillId:'skill-aws'},
  {projectId:'proj-storefront',      skillId:'skill-typescript'},
  {projectId:'proj-storefront',      skillId:'skill-react'},
  {projectId:'proj-storefront',      skillId:'skill-graphql'},
  {projectId:'proj-checkout-sdk',    skillId:'skill-kotlin'},
  {projectId:'proj-checkout-sdk',    skillId:'skill-swift'},
  {projectId:'proj-observability',   skillId:'skill-go'},
  {projectId:'proj-observability',   skillId:'skill-kubernetes'},
  {projectId:'proj-observability',   skillId:'skill-docker'},
  {projectId:'proj-observability',   skillId:'skill-prometheus'},
  {projectId:'proj-feature-flags',   skillId:'skill-rust'},
  {projectId:'proj-feature-flags',   skillId:'skill-postgresql'},
  {projectId:'proj-cdn-edge',        skillId:'skill-go'},
  {projectId:'proj-cdn-edge',        skillId:'skill-redis'},
  {projectId:'proj-cdn-edge',        skillId:'skill-aws'},
  {projectId:'proj-data-warehouse',  skillId:'skill-python'},
  {projectId:'proj-data-warehouse',  skillId:'skill-spark'},
  {projectId:'proj-data-warehouse',  skillId:'skill-kafka'},
  {projectId:'proj-data-warehouse',  skillId:'skill-postgresql'},
  {projectId:'proj-design-system',   skillId:'skill-typescript'},
  {projectId:'proj-design-system',   skillId:'skill-react'},
  {projectId:'proj-design-system',   skillId:'skill-storybook'},
  {projectId:'proj-onboarding',      skillId:'skill-nodejs'},
  {projectId:'proj-onboarding',      skillId:'skill-mongodb'},
  {projectId:'proj-onboarding',      skillId:'skill-graphql'}
] AS row
MATCH (p:Project {id: row.projectId})
MATCH (s:Skill {id: row.skillId})
MERGE (p)-[:USES]->(s);

// ---------------------------------------------------------------------
// Developer -[:KNOWS]-> Developer
// One direction per pair — KNOWS is queried as an undirected pattern.
// dev-glindqvist intentionally has no KNOWS edges: demonstrates the
// "no path found" case for shortest-path / nearest-expert queries.
// ---------------------------------------------------------------------
UNWIND [
  {developerAId:'dev-athompson', developerBId:'dev-okim'},
  {developerAId:'dev-okim',      developerBId:'dev-lchen'},
  {developerAId:'dev-lchen',     developerBId:'dev-spatel'},
  {developerAId:'dev-spatel',    developerBId:'dev-mnakamura'},
  {developerAId:'dev-mnakamura', developerBId:'dev-ejohansson'},
  {developerAId:'dev-ejohansson',developerBId:'dev-cdubois'},
  {developerAId:'dev-cdubois',   developerBId:'dev-hadeyemi'},
  {developerAId:'dev-hadeyemi',  developerBId:'dev-cfischer'},
  {developerAId:'dev-nmartinez', developerBId:'dev-ewright'},
  {developerAId:'dev-ewright',   developerBId:'dev-dkowalski'},
  {developerAId:'dev-dkowalski', developerBId:'dev-anovak'},
  {developerAId:'dev-irossi',    developerBId:'dev-bosei'},
  {developerAId:'dev-bosei',     developerBId:'dev-roconnor'},
  {developerAId:'dev-lchen',     developerBId:'dev-nmartinez'},
  {developerAId:'dev-roconnor',  developerBId:'dev-athompson'},
  {developerAId:'dev-lsilva',    developerBId:'dev-spatel'},
  {developerAId:'dev-athompson', developerBId:'dev-cfischer'}
] AS row
MATCH (a:Developer {id: row.developerAId})
MATCH (b:Developer {id: row.developerBId})
MERGE (a)-[:KNOWS]->(b);
