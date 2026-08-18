import { Sparkles } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { DeveloperCombobox } from '@/components/combobox/DeveloperCombobox'

export function HiddenSkillForm({ developerId, onDeveloperChange, onSubmit }) {
  return (
    <div className="flex flex-col gap-3 sm:flex-row sm:items-end">
      <div className="flex-1 space-y-1.5">
        <Label>Developer</Label>
        <DeveloperCombobox value={developerId} onChange={onDeveloperChange} />
      </div>
      <Button
        className="bg-primary hover:bg-primary-hover"
        disabled={!developerId}
        onClick={() => onSubmit(developerId)}
      >
        <Sparkles className="size-4" />
        Discover Hidden Skills
      </Button>
    </div>
  )
}
