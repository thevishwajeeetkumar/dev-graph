import { Route } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { DeveloperCombobox } from '@/components/combobox/DeveloperCombobox'
import { CompanyCombobox } from '@/components/combobox/CompanyCombobox'

export function ShortestPathForm({ developerId, companyId, onDeveloperChange, onCompanyChange, onSubmit }) {
  return (
    <div className="space-y-3">
      <div className="grid gap-3 sm:grid-cols-2">
        <div className="space-y-1.5">
          <Label>From</Label>
          <DeveloperCombobox value={developerId} onChange={onDeveloperChange} placeholder="Starting developer..." />
        </div>
        <div className="space-y-1.5">
          <Label>To</Label>
          <CompanyCombobox value={companyId} onChange={onCompanyChange} placeholder="Target company..." />
        </div>
      </div>
      <Button
        className="w-full bg-primary hover:bg-primary-hover"
        disabled={!developerId || !companyId}
        onClick={() => onSubmit(developerId, companyId)}
      >
        <Route className="size-4" />
        Find Shortest Path
      </Button>
    </div>
  )
}
