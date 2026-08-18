import { Link } from 'react-router-dom'
import { ArrowRight } from 'lucide-react'
import { Card, CardContent } from '@/components/ui/card'

export function FeatureLauncherCard({ to, icon: Icon, title, description }) {
  return (
    <Link to={to}>
      <Card className="h-full transition-shadow hover:shadow-md">
        <CardContent className="flex h-full flex-col gap-3">
          <span className="flex size-10 items-center justify-center rounded-xl bg-gradient-brand text-primary-foreground">
            <Icon className="size-5" />
          </span>
          <div className="flex-1">
            <p className="font-semibold text-foreground">{title}</p>
            <p className="mt-1 text-sm text-muted-foreground">{description}</p>
          </div>
          <span className="flex items-center gap-1 text-sm font-medium text-primary">
            Explore <ArrowRight className="size-3.5" />
          </span>
        </CardContent>
      </Card>
    </Link>
  )
}
