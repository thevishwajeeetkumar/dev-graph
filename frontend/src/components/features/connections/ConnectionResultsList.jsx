import { useMemo } from 'react'
import { Users2 } from 'lucide-react'
import { useConnections } from '@/hooks/useConnections'
import { LoadingList } from '@/components/common/LoadingList'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { ConnectionResultCard } from '@/components/features/connections/ConnectionResultCard'

function groupByColleague(connections) {
  const groups = new Map()
  for (const record of connections) {
    const key = record.colleague.id
    if (!groups.has(key)) {
      groups.set(key, { colleague: record.colleague, projects: [], companyMap: new Map() })
    }
    const group = groups.get(key)
    group.projects.push(record.sharedProject)
    for (const company of record.companies) {
      group.companyMap.set(company.id, company)
    }
  }
  return Array.from(groups.values()).map((g) => ({
    colleague: g.colleague,
    projects: g.projects,
    companies: Array.from(g.companyMap.values()),
  }))
}

export function ConnectionResultsList({ developerId }) {
  const { data, isPending, isError, error, refetch } = useConnections(developerId)

  const groups = useMemo(() => (data ? groupByColleague(data.connections) : []), [data])

  if (!developerId) {
    return (
      <EmptyState
        icon={Users2}
        title="Pick a developer to get started"
        description="Choose a developer above and discover who they've built projects with, and where those colleagues work."
      />
    )
  }

  if (isPending) return <LoadingList count={4} />
  if (isError) return <ErrorState error={error} onRetry={refetch} />

  if (groups.length === 0) {
    return (
      <EmptyState
        icon={Users2}
        title="No connections found"
        description="This developer hasn't shared a project with anyone else in the graph yet."
      />
    )
  }

  const pageScoped = data.resultCount === data.limit

  return (
    <div className="space-y-3">
      <p className="text-sm text-muted-foreground">
        Found {groups.length} connection{groups.length === 1 ? '' : 's'}
      </p>
      {groups.map((group) => (
        <ConnectionResultCard
          key={group.colleague.id}
          colleague={group.colleague}
          projects={group.projects}
          companies={group.companies}
          pageScoped={pageScoped}
        />
      ))}
    </div>
  )
}
