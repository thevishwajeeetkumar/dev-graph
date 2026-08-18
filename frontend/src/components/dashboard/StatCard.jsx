import { Card, CardContent } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'

export function StatCard({ icon: Icon, label, value, isLoading, accent = 'primary' }) {
  const accentClasses = {
    primary: 'bg-primary/10 text-primary',
    secondary: 'bg-secondary/10 text-secondary',
    success: 'bg-success/10 text-success',
    warning: 'bg-warning/10 text-warning',
  }

  return (
    <Card>
      <CardContent className="flex items-center gap-4 py-2">
        <div className={`flex size-11 shrink-0 items-center justify-center rounded-xl ${accentClasses[accent]}`}>
          <Icon className="size-5" />
        </div>
        <div className="min-w-0">
          <p className="text-sm text-muted-foreground">{label}</p>
          {isLoading ? (
            <Skeleton className="mt-1 h-7 w-16" />
          ) : (
            <p className="text-2xl font-semibold tracking-tight text-foreground">
              {value?.toLocaleString?.() ?? value}
            </p>
          )}
        </div>
      </CardContent>
    </Card>
  )
}
