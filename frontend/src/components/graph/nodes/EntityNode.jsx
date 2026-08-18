import { Handle, Position } from '@xyflow/react'
import { Building2, Code2, Sparkles, User } from 'lucide-react'
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip'
import { GRAPH_NODE_COLORS } from '@/lib/colors'

const ICONS = {
  developer: User,
  company: Building2,
  project: Code2,
  skill: Sparkles,
}

export function EntityNode({ data }) {
  const Icon = ICONS[data.kind] ?? Sparkles
  const color = GRAPH_NODE_COLORS[data.kind]
  const clickable = data.kind === 'developer' && Boolean(data.onClick)

  return (
    <>
      <Handle type="target" position={Position.top} className="opacity-0" />
      <Tooltip>
        <TooltipTrigger asChild>
          <button
            type="button"
            onClick={clickable ? data.onClick : undefined}
            className="flex flex-col items-center gap-1"
            style={{ cursor: clickable ? 'pointer' : 'default' }}
          >
            <span
              className="flex size-9 items-center justify-center rounded-full border-2 bg-card shadow-sm"
              style={{ borderColor: color, color }}
            >
              <Icon className="size-4" />
            </span>
            <span className="max-w-20 truncate text-[10px] font-medium text-muted-foreground">
              {data.label}
            </span>
          </button>
        </TooltipTrigger>
        <TooltipContent>
          <p className="font-medium">{data.label}</p>
          {data.sublabel && <p className="text-xs text-muted-foreground">{data.sublabel}</p>}
        </TooltipContent>
      </Tooltip>
      <Handle type="source" position={Position.bottom} className="opacity-0" />
    </>
  )
}
