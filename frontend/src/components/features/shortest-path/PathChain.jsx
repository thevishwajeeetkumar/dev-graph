import { ArrowRight, Building2, User } from 'lucide-react'

export function PathChain({ nodes }) {
  return (
    <div className="flex flex-wrap items-center justify-center gap-2 rounded-xl border border-border bg-muted/30 p-6">
      {nodes.map((node, index) => (
        <div key={`${node.label}-${node.id}`} className="flex items-center gap-2">
          <div className="flex flex-col items-center gap-1.5">
            <span
              className="flex size-12 items-center justify-center rounded-full border-2 bg-card shadow-sm"
              style={{
                borderColor: node.label === 'Company' ? '#10B981' : '#6366F1',
                color: node.label === 'Company' ? '#10B981' : '#6366F1',
              }}
            >
              {node.label === 'Company' ? <Building2 className="size-5" /> : <User className="size-5" />}
            </span>
            <span className="max-w-24 truncate text-center text-xs font-medium text-foreground">
              {node.name}
            </span>
          </div>
          {index < nodes.length - 1 && <ArrowRight className="size-4 shrink-0 text-muted-foreground" />}
        </div>
      ))}
    </div>
  )
}
