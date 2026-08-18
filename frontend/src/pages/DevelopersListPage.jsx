import { useMemo, useState } from 'react'
import { Search, Users } from 'lucide-react'
import { useDevelopers } from '@/hooks/useDevelopers'
import { DeveloperCard } from '@/components/developer/DeveloperCard'
import { SectionHeader } from '@/components/common/SectionHeader'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { LoadingList } from '@/components/common/LoadingList'

export default function DevelopersListPage() {
  const { data: developers = [], isPending, isError, error, refetch } = useDevelopers()
  const [query, setQuery] = useState('')

  const filtered = useMemo(() => {
    const needle = query.trim().toLowerCase()
    if (!needle) return developers
    return developers.filter((d) =>
      [d.name, d.title, d.location].some((field) => field?.toLowerCase().includes(needle)),
    )
  }, [developers, query])

  return (
    <div className="mx-auto max-w-6xl space-y-6">
      <SectionHeader
        title="Developers"
        description={`${developers.length} developer${developers.length === 1 ? '' : 's'} in the talent graph`}
      />

      <div className="relative max-w-md">
        <Search className="pointer-events-none absolute top-1/2 left-3 size-4 -translate-y-1/2 text-muted-foreground" />
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Filter by name, title, or location..."
          className="h-10 w-full rounded-xl border border-border bg-card pr-3 pl-9 text-sm outline-none placeholder:text-muted-foreground focus-visible:ring-2 focus-visible:ring-ring/50"
        />
      </div>

      {isPending && <LoadingList count={6} />}
      {isError && <ErrorState error={error} onRetry={refetch} />}

      {!isPending && !isError && filtered.length === 0 && (
        <EmptyState
          icon={Users}
          title="No developers match your search"
          description="Try a different name, title, or location."
        />
      )}

      {!isPending && !isError && filtered.length > 0 && (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {filtered.map((developer) => (
            <DeveloperCard key={developer.id} developer={developer} />
          ))}
        </div>
      )}
    </div>
  )
}
