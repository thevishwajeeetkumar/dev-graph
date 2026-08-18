#!/usr/bin/env bash
# Loads the DevGraph sample dataset directly into a running CognoDB /
# Neo4j-compatible instance via cypher-shell, bypassing the Spring Boot app
# entirely. Useful for inspecting/loading the graph with just the database
# running (e.g. in Neo4j Browser or CI), independent of the backend.
#
# Requires cypher-shell on PATH (ships with Neo4j Desktop/Server, or
# install standalone: https://neo4j.com/docs/operations-manual/current/tools/cypher-shell/).
#
# Usage:
#   ./scripts/load-data.sh [bolt-uri] [username] [password]
#   NEO4J_URI=bolt://localhost:7687 NEO4J_USERNAME=neo4j NEO4J_PASSWORD=secret ./scripts/load-data.sh

set -euo pipefail

URI="${1:-${NEO4J_URI:-bolt://localhost:7687}}"
USERNAME="${2:-${NEO4J_USERNAME:-neo4j}}"
PASSWORD="${3:-${NEO4J_PASSWORD:-}}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CYPHER_DIR="$SCRIPT_DIR/../cypher"

if ! command -v cypher-shell >/dev/null 2>&1; then
  echo "cypher-shell not found on PATH. Install it or run the .cypher files" >&2
  echo "in cypher/ manually via Neo4j Browser / the CognoDB console." >&2
  exit 1
fi

echo "Loading DevGraph sample data into $URI ..."

for file in "$CYPHER_DIR/01_constraints.cypher" "$CYPHER_DIR/02_seed_data.cypher"; do
  echo "  -> running $(basename "$file")"
  cypher-shell -a "$URI" -u "$USERNAME" -p "$PASSWORD" --format plain -f "$file"
done

echo "Done. (cypher/03_feature_queries.cypher has the main queries, run separately.)"
