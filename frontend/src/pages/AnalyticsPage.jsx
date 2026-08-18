import { Building2, Code2, Sparkles, Users } from 'lucide-react'
import { useOverviewStats } from '@/hooks/useStats'
import { StatCard } from '@/components/dashboard/StatCard'
import { TopSkillsCard } from '@/components/dashboard/TopSkillsCard'
import { SectionHeader } from '@/components/common/SectionHeader'
import { ErrorState } from '@/components/common/ErrorState'

export default function AnalyticsPage() {
  const { data: stats, isPending, isError, error, refetch } = useOverviewStats(25)

  if (isError) {
    return (
      <div className="mx-auto max-w-4xl space-y-6">
        <SectionHeader title="Analytics" description="Talent graph statistics" />
        <ErrorState error={error} onRetry={refetch} />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-4xl space-y-6">
      <SectionHeader title="Analytics" description="Aggregate statistics across the talent graph" />

      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        <StatCard icon={Users} label="Developers" value={stats?.developerCount} isLoading={isPending} accent="primary" />
        <StatCard icon={Building2} label="Companies" value={stats?.companyCount} isLoading={isPending} accent="success" />
        <StatCard icon={Sparkles} label="Skills" value={stats?.skillCount} isLoading={isPending} accent="secondary" />
        <StatCard icon={Code2} label="Projects" value={stats?.projectCount} isLoading={isPending} accent="warning" />
      </div>

      <TopSkillsCard topSkills={stats?.topSkills} isLoading={isPending} />
    </div>
  )
}
