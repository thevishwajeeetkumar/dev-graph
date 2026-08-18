import { useQuery } from '@tanstack/react-query'
import { getShortestPath } from '@/lib/api'

export function useShortestPath(developerId, companyId, maxHops) {
  return useQuery({
    queryKey: ['graph', 'shortest-path', developerId, companyId, maxHops ?? 6],
    queryFn: () => getShortestPath(developerId, companyId, maxHops),
    enabled: Boolean(developerId) && Boolean(companyId),
  })
}
