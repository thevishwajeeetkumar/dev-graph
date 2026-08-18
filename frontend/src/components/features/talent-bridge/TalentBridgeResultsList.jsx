import { Waypoints } from 'lucide-react'
import { useTalentBridges } from '@/hooks/useTalentBridges'
import { useCompanies } from '@/hooks/useCompanies'
import { LoadingList } from '@/components/common/LoadingList'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { TalentBridgeResultRow } from '@/components/features/talent-bridge/TalentBridgeResultRow'

export function TalentBridgeResultsList({ companyAId, companyBId }) {
  const { data, isPending, isError, error, refetch } = useTalentBridges(companyAId, companyBId)
  const { data: companies = [] } = useCompanies()

  if (!companyAId || !companyBId) {
    return (
      <EmptyState
        icon={Waypoints}
        title="Choose two companies"
        description="DevGraph finds the developers who bridge them - either by working at both, or through a shared colleague."
      />
    )
  }

  if (companyAId === companyBId) return null

  if (isPending) return <LoadingList count={3} />
  if (isError) return <ErrorState error={error} onRetry={refetch} />

  if (data.bridges.length === 0) {
    return (
      <EmptyState
        icon={Waypoints}
        title="No bridges found"
        description="No developer connects these two companies within the current graph."
      />
    )
  }

  const companyAName = companies.find((c) => c.id === companyAId)?.name ?? companyAId
  const companyBName = companies.find((c) => c.id === companyBId)?.name ?? companyBId

  return (
    <div className="space-y-3">
      <p className="text-sm text-muted-foreground">
        Found {data.bridges.length} bridge connection{data.bridges.length === 1 ? '' : 's'}
      </p>
      {data.bridges.map((bridge, index) => (
        <TalentBridgeResultRow
          key={`${bridge.developerA.id}-${bridge.developerB.id}-${index}`}
          bridge={bridge}
          companyAName={companyAName}
          companyBName={companyBName}
        />
      ))}
    </div>
  )
}
