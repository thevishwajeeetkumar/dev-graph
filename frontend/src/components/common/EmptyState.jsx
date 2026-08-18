import { Card, CardContent } from '@/components/ui/card'

export function EmptyState({ icon: Icon, title, description, action }) {
  return (
    <Card className="border-dashed">
      <CardContent className="flex flex-col items-center justify-center gap-3 py-12 text-center">
        {Icon && (
          <div className="flex size-12 items-center justify-center rounded-full bg-muted text-muted-foreground">
            <Icon className="size-6" />
          </div>
        )}
        <div className="space-y-1">
          <p className="font-medium text-foreground">{title}</p>
          {description && (
            <p className="mx-auto max-w-sm text-sm text-muted-foreground">{description}</p>
          )}
        </div>
        {action}
      </CardContent>
    </Card>
  )
}
