import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Card, CardContent } from '@/components/ui/card'
import { SectionHeader } from '@/components/common/SectionHeader'
import { ConnectionDiscoveryForm } from '@/components/features/connections/ConnectionDiscoveryForm'
import { ConnectionResultsList } from '@/components/features/connections/ConnectionResultsList'

export default function ConnectionDiscoveryPage() {
  const [searchParams] = useSearchParams()
  const preset = searchParams.get('developerId') ?? ''

  const [developerId, setDeveloperId] = useState(preset)
  const [searchedId, setSearchedId] = useState(preset)

  return (
    <div className="mx-auto max-w-3xl space-y-6">
      <SectionHeader
        title="Connection Discovery"
        description="Find developers who worked on similar projects and where they work."
      />
      <Card>
        <CardContent>
          <ConnectionDiscoveryForm
            developerId={developerId}
            onDeveloperChange={setDeveloperId}
            onSubmit={setSearchedId}
          />
        </CardContent>
      </Card>
      <ConnectionResultsList developerId={searchedId} />
    </div>
  )
}
