# Why a graph database, honestly

This is not a blanket "graphs beat SQL" argument — three of the five power
features here would run fine as SQL joins. Grouping the features by their
actual reliance on graph traversal, not by vibes:

## Where the graph DB is doing real work

**Nearest Expert Discovery** (`/api/graph/nearest-expert`) and **Shortest
Connection Path** (`/api/graph/shortest-path`) both need a traversal whose
depth isn't known ahead of time — "follow `KNOWS` edges until you reach X,"
where the number of hops could be 1 or could be 8. `shortestPath()` handles
this natively in one traversal.

The relational equivalent is a recursive CTE that walks the relationship
table level by level, tracking visited nodes to avoid cycles, and — for
Nearest Expert specifically — checking a join condition (`HAS_SKILL`) at
every level to know when to stop, rather than against one fixed target id.
That's not "slightly more verbose SQL," it's reimplementing a piece of a
graph engine inside a CTE: no query planner will do that traversal on an
index seek the way a native graph store does, and the bound has to be
capped by hand (see `maxHops`) or the recursion runs away. This is the
one place in the codebase where "graph database" is a technical necessity,
not a modeling preference.

## Where it's convenience, not necessity

**Connection Discovery** (`Developer-[BUILT]->Project<-[BUILT]-Developer
-[WORKED_AT]->Company`) and **Talent Bridge Discovery** (two fixed two-hop
patterns unioned together) are both *fixed-depth* traversals — 3-4 hops,
known in advance. In SQL these are a handful of `JOIN`s (Talent Bridge is
literally a `UNION` of two joins already, mirroring the Cypher). A relational
schema with the right indexes handles both without difficulty. The honest
justification here is ergonomics — relationships as first-class edges
instead of join tables you have to design and maintain — not query-plan
superiority.

**Hidden Skill Discovery** doesn't even use a single Cypher query for its
core logic: it runs two independent reads and computes the set difference
in application code (see the class javadoc for why — a CognoDB pattern-
correlation bug forced this). As shipped, this feature is *not* an argument
for the graph DB; the equivalent `LEFT JOIN ... WHERE x IS NULL` in SQL
would have been simpler than what's here.

## The takeaway

If asked to defend the graph DB choice, lead with Nearest Expert Discovery
and Shortest Path — those are the two places where "just use Postgres"
would mean writing a hand-rolled BFS in recursive SQL. Don't claim the
other three as evidence of the same thing; they demonstrate that a
property graph is a *convenient* way to model this domain, which is a
real but much weaker claim.
