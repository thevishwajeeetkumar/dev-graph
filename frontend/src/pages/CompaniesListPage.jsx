import { Building2 } from 'lucide-react'
import { useCompanies } from '@/hooks/useCompanies'
import { Card, CardContent } from '@/components/ui/card'
import { SectionHeader } from '@/components/common/SectionHeader'
import { EmptyState } from '@/components/common/EmptyState'
import { ErrorState } from '@/components/common/ErrorState'
import { LoadingList } from '@/components/common/LoadingList'

export default function CompaniesListPage() {
  const { data: companies = [], isPending, isError, error, refetch } = useCompanies()

  return (
    <div className="mx-auto max-w-6xl space-y-6">
      <SectionHeader
        title="Companies"
        description={`${companies.length} compan${companies.length === 1 ? 'y' : 'ies'} in the talent graph`}
      />

      {isPending && <LoadingList count={6} />}
      {isError && <ErrorState error={error} onRetry={refetch} />}
      {!isPending && !isError && companies.length === 0 && (
        <EmptyState icon={Building2} title="No companies yet" description="No Company nodes found in the graph." />
      )}

      {!isPending && !isError && companies.length > 0 && (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {companies.map((company) => (
            <Card key={company.id}>
              <CardContent className="flex items-center gap-3">
                <span className="flex size-10 items-center justify-center rounded-lg bg-graph-company/10 text-graph-company">
                  <Building2 className="size-5" />
                </span>
                <p className="font-medium text-foreground">{company.name}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
