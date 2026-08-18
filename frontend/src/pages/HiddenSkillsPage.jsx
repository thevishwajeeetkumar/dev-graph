import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Card, CardContent } from '@/components/ui/card'
import { SectionHeader } from '@/components/common/SectionHeader'
import { HiddenSkillForm } from '@/components/features/hidden-skills/HiddenSkillForm'
import { HiddenSkillResultsList } from '@/components/features/hidden-skills/HiddenSkillResultsList'

export default function HiddenSkillsPage() {
  const [searchParams] = useSearchParams()
  const preset = searchParams.get('developerId') ?? ''

  const [developerId, setDeveloperId] = useState(preset)
  const [searchedId, setSearchedId] = useState(preset)

  return (
    <div className="mx-auto max-w-3xl space-y-6">
      <SectionHeader
        title="Hidden Skill Discovery"
        description="Discover skills developers use in projects but haven't declared."
      />
      <Card>
        <CardContent>
          <HiddenSkillForm developerId={developerId} onDeveloperChange={setDeveloperId} onSubmit={setSearchedId} />
        </CardContent>
      </Card>
      <HiddenSkillResultsList developerId={searchedId} />
    </div>
  )
}
