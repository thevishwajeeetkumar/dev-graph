import { Compass } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { DeveloperCombobox } from '@/components/combobox/DeveloperCombobox'
import { SkillCombobox } from '@/components/combobox/SkillCombobox'

export function NearestExpertForm({ developerId, skillId, onDeveloperChange, onSkillChange, onSubmit }) {
  return (
    <div className="space-y-3">
      <div className="grid gap-3 sm:grid-cols-2">
        <div className="space-y-1.5">
          <Label>Starting developer</Label>
          <DeveloperCombobox value={developerId} onChange={onDeveloperChange} placeholder="Who's looking..." />
        </div>
        <div className="space-y-1.5">
          <Label>Needed skill</Label>
          <SkillCombobox value={skillId} onChange={onSkillChange} placeholder="Target skill..." />
        </div>
      </div>
      <Button
        className="w-full bg-primary hover:bg-primary-hover"
        disabled={!developerId || !skillId}
        onClick={() => onSubmit(developerId, skillId)}
      >
        <Compass className="size-4" />
        Find Nearest Expert
      </Button>
    </div>
  )
}
