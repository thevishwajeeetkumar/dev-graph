import { useQuery } from '@tanstack/react-query'
import { getNearestExpert } from '@/lib/api'

export function useNearestExpert(developerId, skillId, maxHops) {
  return useQuery({
    queryKey: ['graph', 'nearest-expert', developerId, skillId, maxHops ?? 6],
    queryFn: () => getNearestExpert(developerId, skillId, maxHops),
    enabled: Boolean(developerId) && Boolean(skillId),
  })
}
