import { Link } from 'react-router-dom'
import { ArrowRight, Building2, Code2, Share2, Sparkles } from 'lucide-react'
import { Skeleton } from '@/components/ui/skeleton'

function Pill({ icon: Icon, label, value, isLoading, color }) {
  return (
    <div className="flex items-center gap-2.5 rounded-xl border border-border bg-card px-4 py-3">
      <Icon className="size-4" style={{ color }} />
      {isLoading ? (
        <Skeleton className="h-5 w-8" />
      ) : (
        <span className="text-lg font-semibold text-foreground">{value}</span>
      )}
      <span className="text-sm text-muted-foreground">{label}</span>
    </div>
  )
}

export function DeveloperStatPills({ developerId, projectsCount, skillsCount, companiesCount, isLoading }) {
  return (
    <div className="flex flex-wrap gap-3">
      <Pill icon={Code2} label="Projects" value={projectsCount} isLoading={isLoading} color="#F59E0B" />
      <Pill icon={Sparkles} label="Skills" value={skillsCount} isLoading={isLoading} color="#A855F7" />
      <Pill icon={Building2} label="Companies" value={companiesCount} isLoading={isLoading} color="#10B981" />
      <Link
        to={`/connections?developerId=${developerId}`}
        className="flex items-center gap-2 rounded-xl border border-primary/30 bg-primary/5 px-4 py-3 text-sm font-medium text-primary hover:bg-primary/10"
      >
        <Share2 className="size-4" />
        View Connections
        <ArrowRight className="size-3.5" />
      </Link>
    </div>
  )
}
