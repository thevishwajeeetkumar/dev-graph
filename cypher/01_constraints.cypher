// DevGraph — schema constraints
// Safe to re-run: IF NOT EXISTS makes every statement idempotent.
// Run this before 02_seed_data.cypher.

CREATE CONSTRAINT devgraph_developer_id IF NOT EXISTS FOR (d:Developer) REQUIRE d.id IS UNIQUE;
CREATE CONSTRAINT devgraph_skill_id     IF NOT EXISTS FOR (s:Skill)     REQUIRE s.id IS UNIQUE;
CREATE CONSTRAINT devgraph_project_id   IF NOT EXISTS FOR (p:Project)   REQUIRE p.id IS UNIQUE;
CREATE CONSTRAINT devgraph_company_id   IF NOT EXISTS FOR (c:Company)   REQUIRE c.id IS UNIQUE;
CREATE CONSTRAINT devgraph_industry_id  IF NOT EXISTS FOR (i:Industry)  REQUIRE i.id IS UNIQUE;
