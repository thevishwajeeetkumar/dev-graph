import { useQuery } from '@tanstack/react-query'
import { getConnections } from '@/lib/api'

export function useConnections(developerId, offset, limit) {
  return useQuery({
    queryKey: ['graph', 'connections', developerId, offset ?? 0, limit ?? 25],
    queryFn: () => getConnections(developerId, offset, limit),
    enabled: Boolean(developerId),
  })
}
