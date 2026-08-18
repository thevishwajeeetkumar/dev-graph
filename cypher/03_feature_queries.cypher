// DevGraph — the five main graph traversal queries behind the UI's power
// features. Each is copied verbatim from the Java repository that runs it
// (see backend/src/main/java/com/devgraph/repository/), with the
// application's bind parameters ($developerId, etc.) replaced by example
// literal values from the seed dataset so each block can be run as-is in
// Neo4j Browser / cypher-shell / the CognoDB console.
//
// See ../backend/docs/GRAPH_VS_SQL.md for why several of these need a
// graph traversal at all (variable-depth paths a relational join can't
// express without recursive CTEs / a fixed number of self-joins).

// ---------------------------------------------------------------------
// Feature 1 — Professional Connection Discovery
// Developer -> Project -> Developer -> Company
// "Who has Ava Thompson worked alongside, and where do they work now?"
// ---------------------------------------------------------------------
:param developerId => 'dev-athompson';

MATCH (source:Developer {id: $developerId})-[:BUILT]->(project:Project)<-[:BUILT]-(colleague:Developer)
WHERE colleague.id <> $developerId
OPTIONAL MATCH (colleague)-[:WORKED_AT]->(company:Company)
WITH colleague, project, collect(DISTINCT company) AS companies
RETURN colleague, project, companies
ORDER BY colleague.name, project.name
SKIP 0
LIMIT 25;

// ---------------------------------------------------------------------
// Feature 2 — Shortest Connection Path
// Developer -> Developer -> ... -> Company (via KNOWS, then WORKED_AT)
// "What's the shortest chain of colleagues connecting Ava Thompson to Netflix?"
//
// Split into two queries (direct hop-0 check, then a KNOWS*1..N
// shortestPath) rather than one KNOWS*0..N query — see the javadoc on
// ShortestPathRepository for why CognoDB's zero-length variable-length
// relationship handling can't be trusted here.
// ---------------------------------------------------------------------
:param developerId => 'dev-athompson';
:param companyId   => 'co-netflix';

// 2a. already works there (0 hops)
MATCH (start:Developer {id: $developerId})-[:WORKED_AT]->(target:Company {id: $companyId})
RETURN start, target
LIMIT 1;

// 2b. reachable via a KNOWS chain (1..6 hops)
MATCH path = shortestPath(
  (start:Developer {id: $developerId})-[:KNOWS*1..6]-(:Developer)-[:WORKED_AT]->(target:Company {id: $companyId})
)
RETURN path
LIMIT 1;

// ---------------------------------------------------------------------
// Feature 3 — Hidden Skill Discovery
// Developer -> Project -> Skill (used in a project, not on the profile)
// "What has Ava Thompson demonstrably used on a project that isn't listed
// among her declared skills?" (e.g. Kafka, from the Payment Gateway project)
//
// Run as two independent reads and diff in application code rather than
// one WHERE NOT (pattern) query — see HiddenSkillRepository's javadoc for
// why a bound-variable-reused-as-later-target pattern isn't reliable on
// CognoDB. Both reads are included below; the "hidden" set is
// projectSkills minus knownSkillIds.
// ---------------------------------------------------------------------
:param developerId => 'dev-athompson';

// 3a. every skill used on a project this developer built
MATCH (developer:Developer {id: $developerId})-[:BUILT]->(project:Project)-[:USES]->(tech:Skill)
RETURN tech, project
ORDER BY tech.name;

// 3b. every skill already declared on the developer's profile
MATCH (developer:Developer {id: $developerId})-[:HAS_SKILL]->(skill:Skill)
RETURN skill.id AS skillId;

// ---------------------------------------------------------------------
// Feature 4 — Talent Bridge Discovery
// Company A -> Developer -> Developer -> Company B
// "Who connects Spotify and Netflix — either one person who worked at
// both, or two people at each company who know each other?"
// ---------------------------------------------------------------------
:param companyAId => 'co-spotify';
:param companyBId => 'co-netflix';

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
SKIP 0
LIMIT 25;

// ---------------------------------------------------------------------
// Feature 5 — Nearest Expert Discovery
// Developer -[:KNOWS ...]-> nearest colleague who holds a given skill
// "Who is the closest person in Ava Thompson's network that knows Kafka?"
//
// Two-step because the target id isn't known up front: collect every
// developer who holds the skill, then shortestPath from the start
// developer to each candidate and keep the shortest.
// ---------------------------------------------------------------------
:param developerId => 'dev-athompson';
:param skillId      => 'skill-kafka';

// 5a. does the developer already have the skill directly?
MATCH (developer:Developer {id: $developerId})-[:HAS_SKILL]->(skill:Skill {id: $skillId})
RETURN developer
LIMIT 1;

// 5b. everyone else who holds the skill
MATCH (skill:Skill {id: $skillId})<-[:HAS_SKILL]-(expert:Developer)
WHERE expert.id <> $developerId
RETURN DISTINCT expert.id AS expertId;

// 5c. shortest KNOWS path (1..6 hops) to each candidate from 5b, keep the shortest
UNWIND ['dev-ejohansson', 'dev-hadeyemi'] AS expertId
MATCH path = shortestPath(
  (start:Developer {id: $developerId})-[:KNOWS*1..6]-(target:Developer {id: expertId})
)
RETURN path
ORDER BY length(path) ASC
LIMIT 1;

// ---------------------------------------------------------------------
// Handy exploratory queries
// ---------------------------------------------------------------------

// Whole-graph node/relationship counts (used by the dashboard overview stats)
MATCH (d:Developer) WITH count(d) AS developers
MATCH (c:Company)   WITH developers, count(c) AS companies
MATCH (p:Project)   WITH developers, companies, count(p) AS projects
MATCH (s:Skill)     WITH developers, companies, projects, count(s) AS skills
RETURN developers, companies, projects, skills;

// Visualize the whole graph (small enough to render directly)
MATCH (n) RETURN n LIMIT 300;
