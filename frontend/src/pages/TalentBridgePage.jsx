import { useState } from 'react'
import { Card, CardContent } from '@/components/ui/card'
import { SectionHeader } from '@/components/common/SectionHeader'
import { TalentBridgeForm } from '@/components/features/talent-bridge/TalentBridgeForm'
import { TalentBridgeResultsList } from '@/components/features/talent-bridge/TalentBridgeResultsList'

export default function TalentBridgePage() {
  const [companyAId, setCompanyAId] = useState('')
  const [companyBId, setCompanyBId] = useState('')
  const [searched, setSearched] = useState({ companyAId: '', companyBId: '' })

  return (
    <div className="mx-auto max-w-3xl space-y-6">
      <SectionHeader
        title="Talent Bridge Discovery"
        description="Find developers who can connect two companies."
      />
      <Card>
        <CardContent>
          <TalentBridgeForm
            companyAId={companyAId}
            companyBId={companyBId}
            onCompanyAChange={setCompanyAId}
            onCompanyBChange={setCompanyBId}
            onSubmit={(a, b) => setSearched({ companyAId: a, companyBId: b })}
          />
        </CardContent>
      </Card>
      <TalentBridgeResultsList companyAId={searched.companyAId} companyBId={searched.companyBId} />
    </div>
  )
}
