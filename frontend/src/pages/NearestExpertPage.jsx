import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Card, CardContent } from '@/components/ui/card'
import { SectionHeader } from '@/components/common/SectionHeader'
import { NearestExpertForm } from '@/components/features/nearest-expert/NearestExpertForm'
import { NearestExpertResults } from '@/components/features/nearest-expert/NearestExpertResults'

export default function NearestExpertPage() {
  const [searchParams] = useSearchParams()
  const preset = searchParams.get('developerId') ?? ''

  const [developerId, setDeveloperId] = useState(preset)
  const [skillId, setSkillId] = useState('')
  const [searched, setSearched] = useState({ developerId: preset, skillId: '' })

  return (
    <div className="mx-auto max-w-3xl space-y-6">
      <SectionHeader
        title="Nearest Expert Discovery"
        description="Find the closest colleague in your network who already has a skill you need."
      />
      <Card>
        <CardContent>
          <NearestExpertForm
            developerId={developerId}
            skillId={skillId}
            onDeveloperChange={setDeveloperId}
            onSkillChange={setSkillId}
            onSubmit={(d, s) => setSearched({ developerId: d, skillId: s })}
          />
        </CardContent>
      </Card>
      <NearestExpertResults developerId={searched.developerId} skillId={searched.skillId} />
    </div>
  )
}
