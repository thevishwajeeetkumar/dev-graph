import { useState } from 'react'
import { Check, ChevronsUpDown, User } from 'lucide-react'
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
import { useDevelopers } from '@/hooks/useDevelopers'

export function DeveloperCombobox({ value, onChange, placeholder = 'Select a developer...' }) {
  const [open, setOpen] = useState(false)
  const { data: developers = [], isPending } = useDevelopers()
  const selected = developers.find((d) => d.id === value)

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
              <User className="size-4 shrink-0 text-graph-developer" />
              <span className="truncate">{selected.name}</span>
              <span className="truncate text-xs text-muted-foreground">{selected.title}</span>
            </span>
          ) : (
            <span className="text-muted-foreground">{placeholder}</span>
          )}
          <ChevronsUpDown className="size-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-(--radix-popover-trigger-width) p-0" align="start">
        <Command>
          <CommandInput placeholder="Search developers..." />
          <CommandList>
            <CommandEmpty>No developers found.</CommandEmpty>
            <CommandGroup>
              {developers.map((developer) => (
                <CommandItem
                  key={developer.id}
                  value={`${developer.name} ${developer.title} ${developer.location}`}
                  onSelect={() => {
                    onChange(developer.id)
                    setOpen(false)
                  }}
                >
                  <Check
                    className={cn(
                      'size-4',
                      value === developer.id ? 'opacity-100' : 'opacity-0',
                    )}
                  />
                  <div className="min-w-0">
                    <p className="truncate">{developer.name}</p>
                    <p className="truncate text-xs text-muted-foreground">
                      {developer.title} · {developer.location}
                    </p>
                  </div>
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  )
}
