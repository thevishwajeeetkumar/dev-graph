import { useQuery } from '@tanstack/react-query'
import { getHiddenSkills } from '@/lib/api'

export function useHiddenSkills(developerId, offset, limit) {
  return useQuery({
    queryKey: ['graph', 'hidden-skills', developerId, offset ?? 0, limit ?? 25],
    queryFn: () => getHiddenSkills(developerId, offset, limit),
    enabled: Boolean(developerId),
  })
}
