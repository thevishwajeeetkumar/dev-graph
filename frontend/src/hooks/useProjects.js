import { useQuery } from '@tanstack/react-query'
import { getProjects } from '@/lib/api'

export function useProjects(developerId) {
  return useQuery({
    queryKey: ['projects', developerId ?? null],
    queryFn: () => getProjects(developerId),
  })
}
