import { Link } from 'react-router-dom'
import { Building2, MapPin, Route, Share2 } from 'lucide-react'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'

function initials(name) {
  return name
    .split(' ')
    .map((part) => part[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()
}

export function DeveloperHeaderCard({ developer, companies, companiesLoading }) {
  return (
    <Card>
      <CardContent className="flex flex-col gap-6 sm:flex-row sm:items-start sm:justify-between">
        <div className="flex items-start gap-4">
          <Avatar className="size-16">
            <AvatarFallback className="bg-gradient-brand text-lg text-primary-foreground">
              {initials(developer.name)}
            </AvatarFallback>
          </Avatar>
          <div>
            <h1 className="text-xl font-semibold text-foreground">{developer.name}</h1>
            <p className="text-muted-foreground">{developer.title}</p>
            {companiesLoading ? (
              <Skeleton className="mt-1.5 h-4 w-32" />
            ) : companies.length > 0 ? (
              <p className="mt-1.5 flex items-center gap-1.5 text-sm text-foreground/80">
                <Building2 className="size-3.5 text-graph-company" />
                {companies.map((c) => c.name).join(' · ')}
              </p>
            ) : null}
            <p className="mt-1 flex items-center gap-1.5 text-sm text-muted-foreground">
              <MapPin className="size-3.5" />
              {developer.location}
            </p>
          </div>
        </div>
        <div className="flex shrink-0 gap-2">
          <Button asChild variant="outline">
            <Link to={`/shortest-path?developerId=${developer.id}`}>
              <Route className="size-4" />
              Explore Path
            </Link>
          </Button>
          <Button asChild className="bg-primary hover:bg-primary-hover">
            <Link to={`/connections?developerId=${developer.id}`}>
              <Share2 className="size-4" />
              View Connections
            </Link>
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
