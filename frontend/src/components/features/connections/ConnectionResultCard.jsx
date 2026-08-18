import { Link } from 'react-router-dom'
import { Building2 } from 'lucide-react'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent } from '@/components/ui/card'

function initials(name) {
  return name
    .split(' ')
    .map((part) => part[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()
}

export function ConnectionResultCard({ colleague, projects, companies, pageScoped }) {
  return (
    <Card>
      <CardContent className="flex items-start gap-3 py-1">
        <Avatar className="size-10">
          <AvatarFallback className="bg-graph-developer/10 text-graph-developer">
            {initials(colleague.name)}
          </AvatarFallback>
        </Avatar>
        <div className="min-w-0 flex-1">
          <div className="flex flex-wrap items-center justify-between gap-x-3 gap-y-1">
            <Link to={`/developers/${colleague.id}`} className="font-medium text-foreground hover:text-primary">
              {colleague.name}
            </Link>
            <Badge variant="secondary" className="shrink-0">
              {projects.length} shared project{projects.length === 1 ? '' : 's'}
              {pageScoped ? ' (this page)' : ''}
            </Badge>
          </div>
          <p className="text-sm text-muted-foreground">{colleague.title}</p>
          {companies.length > 0 && (
            <p className="mt-1 flex items-center gap-1 text-xs text-muted-foreground">
              <Building2 className="size-3" />
              {companies.map((c) => c.name).join(', ')}
            </p>
          )}
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
