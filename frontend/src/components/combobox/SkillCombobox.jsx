import { useState } from 'react'
import { Check, ChevronsUpDown, Sparkles } from 'lucide-react'
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
import { useSkills } from '@/hooks/useSkills'

export function SkillCombobox({ value, onChange, placeholder = 'Select a skill...' }) {
  const [open, setOpen] = useState(false)
  const { data: skills = [], isPending } = useSkills()
  const selected = skills.find((s) => s.id === value)

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
              <Sparkles className="size-4 shrink-0 text-graph-skill" />
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
          <CommandInput placeholder="Search skills..." />
          <CommandList>
            <CommandEmpty>No skills found.</CommandEmpty>
            <CommandGroup>
              {skills.map((skill) => (
                <CommandItem
                  key={skill.id}
                  value={skill.name}
                  onSelect={() => {
                    onChange(skill.id)
                    setOpen(false)
                  }}
                >
                  <Check
                    className={cn(
                      'size-4',
                      value === skill.id ? 'opacity-100' : 'opacity-0',
                    )}
                  />
                  <span className="truncate">{skill.name}</span>
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  )
}
