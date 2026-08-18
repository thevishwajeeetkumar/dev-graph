import { Search } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { DeveloperCombobox } from '@/components/combobox/DeveloperCombobox'

export function ConnectionDiscoveryForm({ developerId, onDeveloperChange, onSubmit }) {
  return (
    <div className="flex flex-col gap-3 sm:flex-row sm:items-end">
      <div className="flex-1 space-y-1.5">
        <Label>Find connections for</Label>
        <DeveloperCombobox value={developerId} onChange={onDeveloperChange} />
      </div>
      <Button
        className="bg-primary hover:bg-primary-hover"
        disabled={!developerId}
        onClick={() => onSubmit(developerId)}
      >
        <Search className="size-4" />
        Discover Connections
      </Button>
    </div>
  )
}
