import { Code2 } from 'lucide-react'
import { useProjects } from '@/hooks/useProjects'
import { SectionHeader } from '@/components/common/SectionHeader'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { LoadingList } from '@/components/common/LoadingList'
import { DeveloperProjectsSection } from '@/components/developer/DeveloperProjectsSection'

export default function ProjectsListPage() {
  const { data: projects = [], isPending, isError, error, refetch } = useProjects()

  return (
    <div className="mx-auto max-w-4xl space-y-6">
      <SectionHeader
        title="Projects"
        description={`${projects.length} project${projects.length === 1 ? '' : 's'} in the talent graph`}
      />

      {isPending && <LoadingList count={5} />}
      {isError && <ErrorState error={error} onRetry={refetch} />}
      {!isPending && !isError && projects.length === 0 && (
        <EmptyState icon={Code2} title="No projects yet" description="No Project nodes found in the graph." />
      )}
      {!isPending && !isError && projects.length > 0 && (
        <DeveloperProjectsSection projects={projects} isPending={false} />
      )}
    </div>
  )
}
