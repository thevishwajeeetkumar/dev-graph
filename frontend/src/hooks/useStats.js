import { useQuery } from '@tanstack/react-query'
import { getOverviewStats } from '@/lib/api'

export function useOverviewStats(topSkillsLimit) {
  return useQuery({
    queryKey: ['stats', 'overview', topSkillsLimit ?? 5],
    queryFn: () => getOverviewStats(topSkillsLimit),
  })
}
