import { Waypoints } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { CompanyCombobox } from '@/components/combobox/CompanyCombobox'

export function TalentBridgeForm({ companyAId, companyBId, onCompanyAChange, onCompanyBChange, onSubmit }) {
  const invalidPair = companyAId && companyBId && companyAId === companyBId

  return (
    <div className="space-y-3">
      <div className="grid gap-3 sm:grid-cols-2">
        <div className="space-y-1.5">
          <Label>Company A</Label>
          <CompanyCombobox value={companyAId} onChange={onCompanyAChange} excludeId={companyBId} />
        </div>
        <div className="space-y-1.5">
          <Label>Company B</Label>
          <CompanyCombobox value={companyBId} onChange={onCompanyBChange} excludeId={companyAId} />
        </div>
      </div>
      {invalidPair && <p className="text-sm text-destructive">Choose two different companies.</p>}
      <Button
        className="w-full bg-primary hover:bg-primary-hover"
        disabled={!companyAId || !companyBId || invalidPair}
        onClick={() => onSubmit(companyAId, companyBId)}
      >
        <Waypoints className="size-4" />
        Find Bridges
      </Button>
    </div>
  )
}
