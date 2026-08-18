import { Sparkles } from 'lucide-react'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent } from '@/components/ui/card'

export function HiddenSkillResultCard({ skill, projects }) {
  return (
    <Card>
      <CardContent className="flex items-start gap-3 py-1">
        <span className="flex size-10 shrink-0 items-center justify-center rounded-full bg-graph-skill/10 text-graph-skill">
          <Sparkles className="size-4.5" />
        </span>
        <div className="min-w-0 flex-1">
          <div className="flex flex-wrap items-center justify-between gap-x-3 gap-y-1">
            <p className="font-medium text-foreground">{skill.name}</p>
            <Badge variant="outline" className="shrink-0 border-warning/40 text-warning">
              Not in declared skills
            </Badge>
          </div>
          <p className="text-sm text-muted-foreground">
            Used in {projects.length} project{projects.length === 1 ? '' : 's'}
          </p>
          <ul className="mt-2 space-y-0.5">
            {projects.map((project) => (
              <li key={project.id} className="text-sm text-foreground/80">
                &bull; {project.name}
              </li>
            ))}
          </ul>
        </div>
      </CardContent>
    </Card>
  )
}
