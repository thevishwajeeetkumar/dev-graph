# Loads the DevGraph sample dataset directly into a running CognoDB /
# Neo4j-compatible instance via cypher-shell, bypassing the Spring Boot app
# entirely. Useful for inspecting/loading the graph with just the database
# running (e.g. in Neo4j Browser or CI), independent of the backend.
#
# Requires cypher-shell on PATH (ships with Neo4j Desktop/Server, or
# install standalone: https://neo4j.com/docs/operations-manual/current/tools/cypher-shell/).
#
# Usage:
#   .\scripts\load-data.ps1 [-Uri bolt://localhost:7687] [-Username neo4j] [-Password secret]

param(
    [string]$Uri = $(if ($env:NEO4J_URI) { $env:NEO4J_URI } else { "bolt://localhost:7687" }),
    [string]$Username = $(if ($env:NEO4J_USERNAME) { $env:NEO4J_USERNAME } else { "neo4j" }),
    [string]$Password = $(if ($env:NEO4J_PASSWORD) { $env:NEO4J_PASSWORD } else { "" })
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command cypher-shell -ErrorAction SilentlyContinue)) {
    Write-Error "cypher-shell not found on PATH. Install it or run the .cypher files in cypher/ manually via Neo4j Browser / the CognoDB console."
    exit 1
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$cypherDir = Join-Path $scriptDir "..\cypher"

Write-Host "Loading DevGraph sample data into $Uri ..."

foreach ($file in @("01_constraints.cypher", "02_seed_data.cypher")) {
    $path = Join-Path $cypherDir $file
    Write-Host "  -> running $file"
    cypher-shell -a $Uri -u $Username -p $Password --format plain -f $path
}

Write-Host "Done. (cypher/03_feature_queries.cypher has the main queries, run separately.)"
