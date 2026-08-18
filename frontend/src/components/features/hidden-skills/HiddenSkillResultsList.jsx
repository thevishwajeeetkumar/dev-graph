import { Sparkles } from 'lucide-react'
import { useHiddenSkills } from '@/hooks/useHiddenSkills'
import { LoadingList } from '@/components/common/LoadingList'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { HiddenSkillResultCard } from '@/components/features/hidden-skills/HiddenSkillResultCard'

export function HiddenSkillResultsList({ developerId }) {
  const { data, isPending, isError, error, refetch } = useHiddenSkills(developerId)

  if (!developerId) {
    return (
      <EmptyState
        icon={Sparkles}
        title="Pick a developer to get started"
        description="DevGraph compares the technologies used in their projects against their declared skills."
      />
    )
  }

  if (isPending) return <LoadingList count={3} />
  if (isError) return <ErrorState error={error} onRetry={refetch} />

  if (data.hiddenSkills.length === 0) {
    return (
      <EmptyState
        icon={Sparkles}
        title="No hidden skills found"
        description="Every technology used in this developer's projects is already in their declared skill list."
      />
    )
  }

  return (
    <div className="space-y-3">
      <p className="text-sm text-muted-foreground">
        Found {data.hiddenSkills.length} hidden skill{data.hiddenSkills.length === 1 ? '' : 's'}
      </p>
      {data.hiddenSkills.map((record) => (
        <HiddenSkillResultCard key={record.skill.id} skill={record.skill} projects={record.projects} />
      ))}
    </div>
  )
}
