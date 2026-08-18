import { Sparkles } from 'lucide-react'
import { useSkills } from '@/hooks/useSkills'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent } from '@/components/ui/card'
import { SectionHeader } from '@/components/common/SectionHeader'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { Skeleton } from '@/components/ui/skeleton'

export default function SkillsListPage() {
  const { data: skills = [], isPending, isError, error, refetch } = useSkills()

  return (
    <div className="mx-auto max-w-6xl space-y-6">
      <SectionHeader
        title="Skills"
        description={`${skills.length} skill${skills.length === 1 ? '' : 's'} tracked across the talent graph`}
      />

      {isPending && (
        <div className="flex flex-wrap gap-2">
          {Array.from({ length: 12 }).map((_, i) => (
            <Skeleton key={i} className="h-7 w-24 rounded-full" />
          ))}
        </div>
      )}
      {isError && <ErrorState error={error} onRetry={refetch} />}
      {!isPending && !isError && skills.length === 0 && (
        <EmptyState icon={Sparkles} title="No skills yet" description="No Skill nodes found in the graph." />
      )}

      {!isPending && !isError && skills.length > 0 && (
        <Card>
          <CardContent className="flex flex-wrap gap-2">
            {skills.map((skill) => (
              <Badge key={skill.id} variant="secondary" className="bg-graph-skill/10 text-graph-skill hover:bg-graph-skill/10">
                {skill.name}
              </Badge>
            ))}
          </CardContent>
        </Card>
      )}
    </div>
  )
}
