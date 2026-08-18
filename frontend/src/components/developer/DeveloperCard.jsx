import { Link } from 'react-router-dom'
import { MapPin } from 'lucide-react'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Card, CardContent } from '@/components/ui/card'

function initials(name) {
  return name
    .split(' ')
    .map((part) => part[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()
}

export function DeveloperCard({ developer }) {
  return (
    <Link to={`/developers/${developer.id}`}>
      <Card className="h-full transition-shadow hover:shadow-md">
        <CardContent className="flex items-center gap-3">
          <Avatar className="size-11">
            <AvatarFallback className="bg-gradient-brand text-primary-foreground">
              {initials(developer.name)}
            </AvatarFallback>
          </Avatar>
          <div className="min-w-0">
            <p className="truncate font-medium text-foreground">{developer.name}</p>
            <p className="truncate text-sm text-muted-foreground">{developer.title}</p>
            <p className="mt-0.5 flex items-center gap-1 truncate text-xs text-muted-foreground">
              <MapPin className="size-3" />
              {developer.location}
            </p>
          </div>
        </CardContent>
      </Card>
    </Link>
  )
}
