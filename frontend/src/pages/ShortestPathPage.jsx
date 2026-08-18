import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Card, CardContent } from '@/components/ui/card'
import { SectionHeader } from '@/components/common/SectionHeader'
import { ShortestPathForm } from '@/components/features/shortest-path/ShortestPathForm'
import { ShortestPathResults } from '@/components/features/shortest-path/ShortestPathResults'

export default function ShortestPathPage() {
  const [searchParams] = useSearchParams()
  const preset = searchParams.get('developerId') ?? ''

  const [developerId, setDeveloperId] = useState(preset)
  const [companyId, setCompanyId] = useState('')
  const [searched, setSearched] = useState({ developerId: preset, companyId: '' })

  return (
    <div className="mx-auto max-w-3xl space-y-6">
      <SectionHeader
        title="Shortest Connection Path"
        description="Find the shortest path to connect to a target company or developer."
      />
      <Card>
        <CardContent>
          <ShortestPathForm
            developerId={developerId}
            companyId={companyId}
            onDeveloperChange={setDeveloperId}
            onCompanyChange={setCompanyId}
            onSubmit={(d, c) => setSearched({ developerId: d, companyId: c })}
          />
        </CardContent>
      </Card>
      <ShortestPathResults developerId={searched.developerId} companyId={searched.companyId} />
    </div>
  )
}
