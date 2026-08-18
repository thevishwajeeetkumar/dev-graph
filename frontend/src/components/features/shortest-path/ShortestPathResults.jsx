import { Route, Unlink } from 'lucide-react'
import { useShortestPath } from '@/hooks/useShortestPath'
import { LoadingList } from '@/components/common/LoadingList'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { Badge } from '@/components/ui/badge'
import { PathChain } from '@/components/features/shortest-path/PathChain'
import { PathDetailsList } from '@/components/features/shortest-path/PathDetailsList'

export function ShortestPathResults({ developerId, companyId }) {
  const { data, isPending, isError, error, refetch } = useShortestPath(developerId, companyId)

  if (!developerId || !companyId) {
    return (
      <EmptyState
        icon={Route}
        title="Choose a starting developer and a target company"
        description="DevGraph will trace the shortest chain of colleague connections that reaches someone at that company."
      />
    )
  }

  if (isPending) return <LoadingList count={1} />
  if (isError) return <ErrorState error={error} onRetry={refetch} />

  if (!data.pathFound) {
    return (
      <EmptyState
        icon={Unlink}
        title="No path found"
        description="There's no chain of known colleagues connecting this developer to that company within the search depth."
      />
    )
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-2">
        <Badge className="bg-success/10 text-success hover:bg-success/10">Path found</Badge>
        <span className="text-sm text-muted-foreground">
          {data.hops} hop{data.hops === 1 ? '' : 's'}
        </span>
      </div>
      <PathChain nodes={data.nodes} />
      <PathDetailsList nodes={data.nodes} relationshipTypes={data.relationshipTypes} />
    </div>
  )
}
