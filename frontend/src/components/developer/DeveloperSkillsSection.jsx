import { Sparkles } from 'lucide-react'
import { Badge } from '@/components/ui/badge'
import { Skeleton } from '@/components/ui/skeleton'
import { EmptyState } from '@/components/common/EmptyState'

export function DeveloperSkillsSection({ skills, isPending }) {
  if (isPending) {
    return (
      <div className="flex flex-wrap gap-2">
        {Array.from({ length: 6 }).map((_, i) => (
          <Skeleton key={i} className="h-6 w-20 rounded-full" />
        ))}
      </div>
    )
  }

  if (skills.length === 0) {
    return <EmptyState icon={Sparkles} title="No declared skills" description="This developer has no HAS_SKILL relationships recorded yet." />
  }

  return (
    <div className="flex flex-wrap gap-2">
      {skills.map((skill) => (
        <Badge key={skill.id} variant="secondary" className="bg-graph-skill/10 text-graph-skill hover:bg-graph-skill/10">
          {skill.name}
        </Badge>
      ))}
    </div>
  )
}
