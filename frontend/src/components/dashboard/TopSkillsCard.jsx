import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'

export function TopSkillsCard({ topSkills, isLoading }) {
  const max = topSkills?.[0]?.developerCount ?? 1

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-base">Top Skills</CardTitle>
      </CardHeader>
      <CardContent className="space-y-3">
        {isLoading &&
          Array.from({ length: 5 }).map((_, i) => <Skeleton key={i} className="h-8 w-full" />)}

        {!isLoading && topSkills?.length === 0 && (
          <p className="text-sm text-muted-foreground">No skills recorded yet.</p>
        )}

        {!isLoading &&
          topSkills?.map(({ skill, developerCount }) => (
            <div key={skill.id} className="space-y-1">
              <div className="flex items-center justify-between text-sm">
                <span className="font-medium text-foreground">{skill.name}</span>
                <span className="text-muted-foreground">{developerCount}</span>
              </div>
              <div className="h-2 overflow-hidden rounded-full bg-muted">
                <div
                  className="h-full rounded-full bg-gradient-brand"
                  style={{ width: `${Math.max(6, (developerCount / max) * 100)}%` }}
                />
              </div>
            </div>
          ))}
      </CardContent>
    </Card>
  )
}
