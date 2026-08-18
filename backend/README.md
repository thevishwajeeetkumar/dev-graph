# DevGraph Backend

Developer Talent Graph Explorer API — Spring Boot 3 on top of CognoDB (a Bolt
5.x-compatible graph database, queried via the official Neo4j Java driver).

## Prerequisites

- JDK 21+ (the bundled `./mvnw` / `mvnw.cmd` wrapper handles Maven itself)
- A running CognoDB / Neo4j-compatible instance reachable over Bolt
- Docker (optional, for containerized runs)

## Setup

```bash
cp .env.example .env
# edit .env with your CognoDB URI/credentials
```

`.env` is gitignored — real credentials never go in `application.yml`. Every
config value has an environment variable behind it (see `.env.example` for
the full list); the checked-in defaults only work against a local dev
instance (`bolt://localhost:7687`, empty password) and are not meant to
reach a real deployment.

## Running locally

Export the variables from `.env` into your shell, then run:

```bash
# bash/zsh
export $(grep -v '^#' .env | xargs)
./mvnw spring-boot:run

# PowerShell
Get-Content .env | ForEach-Object {
  if ($_ -match '^\s*([^#][^=]*)=(.*)$') { Set-Item "Env:$($matches[1])" $matches[2] }
}
.\mvnw.cmd spring-boot:run
```

The API listens on `http://localhost:8080` (or `$SERVER_PORT`).

## Seeding sample data

`seed.DataSeeder` loads a realistic, densely-connected sample graph (18
developers, 6 companies, 5 industries, 22 skills, 10 projects, and all six
relationship types) - sized specifically to exercise all four power
features, not just satisfy the schema: multi-builder projects for
Connection Discovery, cross-company `KNOWS` chains (plus one deliberately
isolated developer) for Shortest Path, developers missing 1-2 of the
skills their own projects use for Hidden Skill Discovery, and companies
bridged both directly and via `KNOWS` for Talent Bridge. See
`seed/SeedData.java` for the exact dataset and the reasoning behind it.

It's off by default. To run it once against your CognoDB instance:

```bash
DEVGRAPH_SEED_ENABLED=true ./mvnw spring-boot:run
```

Every node/relationship is loaded via Cypher `MERGE` keyed on a stable
business `id`, so it's safe to re-run - it re-applies the same data rather
than duplicating it. To wipe the database first (DANGER - deletes
everything, only ever point this at a scratch/dev instance):

```bash
DEVGRAPH_SEED_ENABLED=true DEVGRAPH_SEED_RESET=true ./mvnw spring-boot:run
```

On completion it logs node and relationship counts so you can confirm what
landed without leaving the terminal:

```
INFO  c.d.seed.DataSeeder : Seeding complete in 20463 ms. Node counts: Developer=18, Skill=22, Project=10, Company=6, Industry=5
INFO  c.d.seed.DataSeeder : Relationship counts: HAS_SKILL=51, WORKED_AT=20, BUILT=20, USES=32, IN_INDUSTRY=6, KNOWS=17
```

Verify it landed correctly by hitting the API against the real data, e.g.:

```bash
curl "http://localhost:8080/api/graph/hidden-skills?developerId=dev-athompson"
# -> Kafka (used by Payment Gateway Revamp, not in her HAS_SKILL)
curl "http://localhost:8080/api/graph/shortest-path?developerId=dev-athompson&companyId=co-netflix"
# -> 2-hop path through Chloe Fischer (KNOWS -> WORKED_AT)
curl "http://localhost:8080/api/graph/talent-bridge?companyAId=co-shopify&companyBId=co-spotify"
# -> DIRECT bridge via Mia Nakamura, who worked at both
```

### A CognoDB quirk worth knowing about

While validating the seeded data, `WHERE NOT (pattern)`, `exists(pattern)`,
and `OPTIONAL MATCH` all turned out to give wrong results here whenever the
pattern's target was a variable already bound earlier in the same
query - CognoDB re-resolves it as if unbound instead of correlating it,
which either silently drops correct rows or cartesian-multiplies them.
Confirmed against real seeded data, not a hunch - see the `HiddenSkillRepository`
and `ShortestPathRepository` javadoc for the specific before/after queries.
The fix in both cases: never reuse an earlier-bound variable as a later
pattern's target - match it fresh by id inline in the same clause, or (for
the negated-skill-set case) do the correlation as two independent reads and
a set difference in Java instead of one Cypher query.

## Tests

```bash
./mvnw test
```

Unit tests mock the repository layer (Mockito) and exercise each service's
happy path plus its not-found/invalid-request edge cases — no live database
needed. `DevGraphApplicationTests` boots the full Spring context to catch
wiring mistakes.

## Docker

```bash
docker build -t devgraph-backend .
docker run --rm -p 8080:8080 \
  -e NEO4J_URI=bolt://host.docker.internal:7687 \
  -e NEO4J_USERNAME=neo4j \
  -e NEO4J_PASSWORD=changeme \
  -e APP_CORS_ALLOWED_ORIGINS=https://your-frontend.example.com \
  devgraph-backend
```

The image bakes in no credentials — everything comes from the environment
at `docker run` time.

For Kubernetes (or any orchestrator with separate probe support), point
readiness at `/actuator/health/readiness` and liveness at
`/actuator/health/liveness` - not both at the same path:

```yaml
readinessProbe:
  httpGet: { path: /actuator/health/readiness, port: 8080 }
livenessProbe:
  httpGet: { path: /actuator/health/liveness, port: 8080 }
```

## API

Every endpoint returns the same envelope:

```json
{ "success": true, "data": { ... }, "error": null, "timestamp": "2026-08-18T01:00:00Z" }
```

or, on failure:

```json
{ "success": false, "data": null, "error": { "status": 404, "error": "Not Found", "message": "Developer not found: dev-1" }, "timestamp": "..." }
```

| Method | Path | Description |
|---|---|---|
| GET | `/api/health` | CognoDB connectivity check, app-shaped (`ApiResponse<HealthStatus>`). 200 if reachable, 503 if not. |
| GET | `/actuator/health` | Aggregate Actuator health (unauthenticated callers get bare `{"status":...}`, no component detail). |
| GET | `/actuator/health/readiness` | Readiness probe - includes the CognoDB check. 503 the instant CognoDB is unreachable, so an orchestrator/load balancer stops routing traffic *before* a request fails, not after. |
| GET | `/actuator/health/liveness` | Liveness probe - JVM-only, deliberately excludes CognoDB. A DB blip shouldn't get the pod killed/restarted, only taken out of rotation via readiness. |
| GET | `/api/developers` | List all developers. |
| GET | `/api/developers/{developerId}` | Get one developer. 404 if missing. |
| GET | `/api/graph/connections?developerId=&offset=&limit=` | Feature 1: Professional Connection Discovery (`Developer -> Project -> Developer -> Company`). |
| GET | `/api/graph/shortest-path?developerId=&companyId=&maxHops=` | Feature 2: Shortest Connection Path (`Developer -> Developer -> ... -> Company`, via `KNOWS`). |
| GET | `/api/graph/hidden-skills?developerId=&offset=&limit=` | Feature 3: Hidden Skill Discovery (tech used in a project but not in the developer's declared skills). |
| GET | `/api/graph/talent-bridge?companyAId=&companyBId=&offset=&limit=` | Feature 4: Talent Bridge Discovery (`Company A -> Developer -> Developer -> Company B`). |
| GET | `/api/graph/nearest-expert?developerId=&skillId=&maxHops=` | Feature 5: Nearest Expert Discovery (`Developer -[KNOWS ...]-> nearest colleague who holds a given skill`). |

`limit` defaults to 25, capped at 100. `offset` defaults to 0. `maxHops`
defaults to 6, capped at 10. Out-of-range values return 400, not a silent
clamp.

`resultCount` on list responses is the number of items in the current page,
not a global total across all pages (no separate `COUNT` query is run).

### Error status codes

| Status | Meaning |
|---|---|
| 400 | Bad input (blank/out-of-range param, malformed request). |
| 404 | Developer/Company id doesn't exist. |
| 405 | Wrong HTTP method for the path. |
| 500 | Unexpected server-side bug. |
| 502 | CognoDB reported an error processing an otherwise valid query. |
| 503 | CognoDB is unreachable — retry with backoff. |

Stack traces are never returned to the client; full details are logged
server-side only (SLF4J, `com.devgraph` at `INFO` by default, `LOG_LEVEL` to
override).

### Logs

Console output is colorized by level (green `INFO`, yellow `WARN`, red
`ERROR`) via `spring.output.ansi.enabled: ALWAYS` — set
`SPRING_OUTPUT_ANSI_ENABLED=NEVER` for log aggregators that don't want raw
ANSI escape codes in captured output.

On every startup, `CognoDbConnectivityLogger` does a one-time connectivity
check and logs the outcome plainly:

```
INFO  c.d.config.CognoDbConnectivityLogger : CognoDB connectivity check PASSED - connected to bolt://localhost:7687 (42 ms)
ERROR c.d.config.CognoDbConnectivityLogger : CognoDB connectivity check FAILED - could not reach bolt://localhost:7687 (ServiceUnavailableException: ...)
```

A FAILED check does not crash startup — `/actuator/health/readiness` is
what gates traffic (see above), so a database that's just slow to come up
(e.g. in docker-compose) doesn't crash-loop the app.

## Why a graph database

See [`docs/GRAPH_VS_SQL.md`](docs/GRAPH_VS_SQL.md) for an honest, feature-by-
feature breakdown of where the graph model is actually necessary (variable-
depth traversal) versus where it's convenience over a relational schema.

## Architecture notes

- **Layering**: `controller` → `service` → `repository`. Controllers only
  bind/validate transport-level input and wrap the result in `ApiResponse`;
  all Cypher lives in `repository`, all business rules (limits, existence
  checks, semantic validation) live in `service`.
- **Exceptions**: everything thrown by application code extends
  `DevGraphException`, which carries its own `HttpStatus`. Raw
  `org.neo4j.driver.exceptions.*` never escape the repository layer — they're
  translated into `GraphDatabaseUnavailableException` (503) or
  `GraphDatabaseQueryException` (502) at the driver boundary in
  `Neo4jRepositorySupport`.
- **Sessions**: every `Session` is opened in a try-with-resources block in
  `Neo4jRepositorySupport.read`/`write` — nothing outside that class ever
  touches the driver directly (except `HealthService`, for a lightweight
  connectivity check that isn't a Cypher query).
- **Parameterized queries**: every query parameter is bound via the driver's
  `Map<String,Object>` params, never string-concatenated. The two Cypher
  features that syntactically can't take a parameter — node labels, and the
  `*min..max` bound on a variable-length relationship — are restricted to a
  fixed `NodeLabel` enum and a server-validated integer range respectively,
  so nothing user-controlled ever reaches a query string directly.
