import { Compass, SearchX } from 'lucide-react'
import { useNearestExpert } from '@/hooks/useNearestExpert'
import { LoadingList } from '@/components/common/LoadingList'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { Badge } from '@/components/ui/badge'
import { PathChain } from '@/components/features/shortest-path/PathChain'
import { PathDetailsList } from '@/components/features/shortest-path/PathDetailsList'

export function NearestExpertResults({ developerId, skillId }) {
  const { data, isPending, isError, error, refetch } = useNearestExpert(developerId, skillId)

  if (!developerId || !skillId) {
    return (
      <EmptyState
        icon={Compass}
        title="Choose a developer and a skill"
        description="DevGraph will trace the shortest KNOWS chain to the nearest colleague who already has that skill."
      />
    )
  }

  if (isPending) return <LoadingList count={1} />
  if (isError) return <ErrorState error={error} onRetry={refetch} />

  if (!data.pathFound) {
    return (
      <EmptyState
        icon={SearchX}
        title="No expert found"
        description={`No one reachable within the search depth holds ${data.matchedSkill?.name ?? 'that skill'}.`}
      />
    )
  }

  if (data.hops === 0) {
    return (
      <div className="space-y-4">
        <Badge className="bg-success/10 text-success hover:bg-success/10">Already an expert</Badge>
        <p className="text-sm text-muted-foreground">
          {data.nodes[0]?.name} already has {data.matchedSkill?.name} — no one to route through.
        </p>
      </div>
    )
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-2">
        <Badge className="bg-success/10 text-success hover:bg-success/10">Expert found</Badge>
        <span className="text-sm text-muted-foreground">
          {data.hops} hop{data.hops === 1 ? '' : 's'} · {data.matchedSkill?.name}
        </span>
      </div>
      <PathChain nodes={data.nodes} />
      <PathDetailsList nodes={data.nodes} relationshipTypes={data.relationshipTypes} />
    </div>
  )
}
