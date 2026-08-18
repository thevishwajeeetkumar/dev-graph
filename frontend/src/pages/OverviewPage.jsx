import { Building2, Code2, Compass, Route, Share2, Sparkles, Users, Waypoints } from 'lucide-react'
import { useOverviewStats } from '@/hooks/useStats'
import { StatCard } from '@/components/dashboard/StatCard'
import { TopSkillsCard } from '@/components/dashboard/TopSkillsCard'
import { FeatureLauncherCard } from '@/components/dashboard/FeatureLauncherCard'
import { GraphOverviewWidget } from '@/components/graph/GraphOverviewWidget'
import { GlobalSearchBar } from '@/components/layout/GlobalSearchBar'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { ErrorState } from '@/components/common/ErrorState'

const FEATURES = [
  {
    to: '/connections',
    icon: Share2,
    title: 'Connection Discovery',
    description: 'Find developers who worked on similar projects and where they work.',
  },
  {
    to: '/shortest-path',
    icon: Route,
    title: 'Shortest Connection Path',
    description: 'Find the shortest chain of connections to reach a target company.',
  },
  {
    to: '/hidden-skills',
    icon: Sparkles,
    title: 'Hidden Skill Discovery',
    description: "Discover skills developers use in projects but haven't declared.",
  },
  {
    to: '/talent-bridge',
    icon: Waypoints,
    title: 'Talent Bridge Discovery',
    description: 'Find developers who bridge two companies through their network.',
  },
  {
    to: '/nearest-expert',
    icon: Compass,
    title: 'Nearest Expert Discovery',
    description: 'Find the closest colleague in your network who already has a skill you need.',
  },
]

export default function OverviewPage() {
  const { data: stats, isPending, isError, error, refetch } = useOverviewStats(5)

  return (
    <div className="mx-auto max-w-6xl space-y-6">
      <div>
        <h1 className="text-2xl font-semibold text-foreground">Welcome back, Sarah 👋</h1>
        <p className="text-muted-foreground">Explore developer talent and uncover powerful connections</p>
      </div>

      <GlobalSearchBar />

      {isError ? (
        <ErrorState error={error} onRetry={refetch} />
      ) : (
        <>
          <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
            <StatCard icon={Users} label="Developers" value={stats?.developerCount} isLoading={isPending} accent="primary" />
            <StatCard icon={Building2} label="Companies" value={stats?.companyCount} isLoading={isPending} accent="success" />
            <StatCard icon={Sparkles} label="Skills" value={stats?.skillCount} isLoading={isPending} accent="secondary" />
            <StatCard icon={Code2} label="Projects" value={stats?.projectCount} isLoading={isPending} accent="warning" />
          </div>

          <div className="grid gap-4 lg:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle className="text-base">Graph Overview</CardTitle>
                <p className="text-sm text-muted-foreground">Visualize the talent network</p>
              </CardHeader>
              <CardContent>
                {isPending ? (
                  <div className="h-[260px] animate-pulse rounded-xl bg-muted" />
                ) : (
                  <GraphOverviewWidget stats={stats} />
                )}
              </CardContent>
            </Card>
            <TopSkillsCard topSkills={stats?.topSkills} isLoading={isPending} />
          </div>
        </>
      )}

      <div>
        <h2 className="mb-3 text-lg font-semibold text-foreground">Power features</h2>
        <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {FEATURES.map((feature) => (
            <FeatureLauncherCard key={feature.to} {...feature} />
          ))}
        </div>
      </div>
    </div>
  )
}
