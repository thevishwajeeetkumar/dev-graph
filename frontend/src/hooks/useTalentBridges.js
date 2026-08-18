import { useQuery } from '@tanstack/react-query'
import { getTalentBridges } from '@/lib/api'

export function useTalentBridges(companyAId, companyBId, offset, limit) {
  return useQuery({
    queryKey: ['graph', 'talent-bridge', companyAId, companyBId, offset ?? 0, limit ?? 25],
    queryFn: () => getTalentBridges(companyAId, companyBId, offset, limit),
    enabled: Boolean(companyAId) && Boolean(companyBId) && companyAId !== companyBId,
  })
}
