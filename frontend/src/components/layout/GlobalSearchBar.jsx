import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Building2, Search, Sparkles, User } from 'lucide-react'
import { Popover, PopoverContent, PopoverAnchor } from '@/components/ui/popover'
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command'
import { useDevelopers } from '@/hooks/useDevelopers'
import { useCompanies } from '@/hooks/useCompanies'
import { useSkills } from '@/hooks/useSkills'

// Client-side only: the backend has no search endpoint, so this filters
// whatever /api/developers, /api/companies and /api/skills already returned.
export function GlobalSearchBar() {
  const navigate = useNavigate()
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState('')

  const { data: developers = [] } = useDevelopers()
  const { data: companies = [] } = useCompanies()
  const { data: skills = [] } = useSkills()

  function go(path) {
    setOpen(false)
    setQuery('')
    navigate(path)
  }

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverAnchor asChild>
        <div className="relative w-full max-w-md">
          <Search className="pointer-events-none absolute top-1/2 left-3 size-4 -translate-y-1/2 text-muted-foreground" />
          <input
            value={query}
            onChange={(event) => {
              setQuery(event.target.value)
              setOpen(true)
            }}
            onFocus={() => query && setOpen(true)}
            placeholder="Search developers, skills, companies..."
            className="h-10 w-full rounded-xl border border-border bg-card pr-3 pl-9 text-sm outline-none placeholder:text-muted-foreground focus-visible:ring-2 focus-visible:ring-ring/50"
          />
        </div>
      </PopoverAnchor>
      <PopoverContent
        align="start"
        className="w-(--radix-popover-trigger-width) min-w-80 p-0"
        onOpenAutoFocus={(event) => event.preventDefault()}
      >
        <Command shouldFilter={false}>
          <CommandInput value={query} onValueChange={setQuery} placeholder="Type to search..." className="hidden" />
          <CommandList>
            <CommandEmpty>No matches yet.</CommandEmpty>
            <CommandGroup heading="Developers">
              {developers
                .filter((d) => matches(query, [d.name, d.title, d.location]))
                .slice(0, 5)
                .map((d) => (
                  <CommandItem key={d.id} value={d.name} onSelect={() => go(`/developers/${d.id}`)}>
                    <User className="size-4 text-graph-developer" />
                    <span>{d.name}</span>
                    <span className="ml-auto text-xs text-muted-foreground">{d.title}</span>
                  </CommandItem>
                ))}
            </CommandGroup>
            <CommandGroup heading="Companies">
              {companies
                .filter((c) => matches(query, [c.name]))
                .slice(0, 5)
                .map((c) => (
                  <CommandItem key={c.id} value={c.name} onSelect={() => go('/companies')}>
                    <Building2 className="size-4 text-graph-company" />
                    <span>{c.name}</span>
                  </CommandItem>
                ))}
            </CommandGroup>
            <CommandGroup heading="Skills">
              {skills
                .filter((s) => matches(query, [s.name]))
                .slice(0, 5)
                .map((s) => (
                  <CommandItem key={s.id} value={s.name} onSelect={() => go('/skills')}>
                    <Sparkles className="size-4 text-graph-skill" />
                    <span>{s.name}</span>
                  </CommandItem>
                ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  )
}

function matches(query, fields) {
  if (!query.trim()) return false
  const needle = query.trim().toLowerCase()
  return fields.some((field) => field?.toLowerCase().includes(needle))
}
