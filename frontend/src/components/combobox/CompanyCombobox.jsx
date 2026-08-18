import { useState } from 'react'
import { Building2, Check, ChevronsUpDown } from 'lucide-react'
import { Button } from '@/components/ui/button'
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { Skeleton } from '@/components/ui/skeleton'
import { cn } from '@/lib/utils'
import { useCompanies } from '@/hooks/useCompanies'

export function CompanyCombobox({ value, onChange, placeholder = 'Select a company...', excludeId }) {
  const [open, setOpen] = useState(false)
  const { data: allCompanies = [], isPending } = useCompanies()
  const companies = excludeId ? allCompanies.filter((c) => c.id !== excludeId) : allCompanies
  const selected = allCompanies.find((c) => c.id === value)

  if (isPending) {
    return <Skeleton className="h-10 w-full" />
  }

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-full justify-between font-normal"
        >
          {selected ? (
            <span className="flex min-w-0 items-center gap-2">
              <Building2 className="size-4 shrink-0 text-graph-company" />
              <span className="truncate">{selected.name}</span>
            </span>
          ) : (
            <span className="text-muted-foreground">{placeholder}</span>
          )}
          <ChevronsUpDown className="size-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-(--radix-popover-trigger-width) p-0" align="start">
        <Command>
          <CommandInput placeholder="Search companies..." />
          <CommandList>
            <CommandEmpty>No companies found.</CommandEmpty>
            <CommandGroup>
              {companies.map((company) => (
                <CommandItem
                  key={company.id}
                  value={company.name}
                  onSelect={() => {
                    onChange(company.id)
                    setOpen(false)
                  }}
                >
                  <Check className={cn('size-4', value === company.id ? 'opacity-100' : 'opacity-0')} />
                  <span>{company.name}</span>
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  )
}
