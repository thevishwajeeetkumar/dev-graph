import { Code2 } from 'lucide-react'
import { Card, CardContent } from '@/components/ui/card'
import { LoadingList } from '@/components/common/LoadingList'
import { EmptyState } from '@/components/common/EmptyState'

export function DeveloperProjectsSection({ projects, isPending }) {
  if (isPending) return <LoadingList count={3} />

  if (projects.length === 0) {
    return <EmptyState icon={Code2} title="No projects yet" description="No BUILT relationships recorded for this developer." />
  }

  return (
    <div className="space-y-3">
      {projects.map((project) => (
        <Card key={project.id}>
          <CardContent className="flex items-start gap-3 py-1">
            <span className="flex size-9 shrink-0 items-center justify-center rounded-lg bg-graph-project/10 text-graph-project">
              <Code2 className="size-4" />
            </span>
            <div className="min-w-0">
              <p className="font-medium text-foreground">{project.name}</p>
              {project.description && (
                <p className="text-sm text-muted-foreground">{project.description}</p>
              )}
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}
