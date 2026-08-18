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

export function TalentBridgeResultRow({ bridge, companyAName, companyBName }) {
  const { developerA, developerB, bridgeType } = bridge
  const isDirect = bridgeType === 'DIRECT'

  return (
    <Card>
      <CardContent className="flex items-center gap-3 py-1">
        <Avatar className="size-9">
          <AvatarFallback className="bg-graph-developer/10 text-graph-developer text-xs">
            {initials(developerA.name)}
          </AvatarFallback>
        </Avatar>
        {!isDirect && (
          <Avatar className="-ml-4 size-9 border-2 border-card">
            <AvatarFallback className="bg-graph-developer/10 text-graph-developer text-xs">
              {initials(developerB.name)}
            </AvatarFallback>
          </Avatar>
        )}

        <div className="min-w-0 flex-1">
          <p className="text-sm text-foreground">
            {isDirect ? (
              <>
                <Link to={`/developers/${developerA.id}`} className="font-medium hover:text-primary">
                  {developerA.name}
                </Link>{' '}
                worked at both {companyAName} and {companyBName}.
              </>
            ) : (
              <>
                <Link to={`/developers/${developerA.id}`} className="font-medium hover:text-primary">
                  {developerA.name}
                </Link>{' '}
                (worked at {companyAName}) is connected to{' '}
                <Link to={`/developers/${developerB.id}`} className="font-medium hover:text-primary">
                  {developerB.name}
                </Link>{' '}
                (worked at {companyBName}).
              </>
            )}
          </p>
          <Badge variant="secondary" className="mt-1">
            {isDirect ? 'Worked at both companies' : 'Connected via a shared colleague'}
          </Badge>
        </div>

        <div className="flex shrink-0 items-center gap-1.5 text-muted-foreground">
          <Building2 className="size-4 text-graph-company" />
          <span className="text-xs">A</span>
          <span className="text-xs">&rarr;</span>
          <Building2 className="size-4 text-graph-company" />
          <span className="text-xs">B</span>
        </div>
      </CardContent>
    </Card>
  )
}
