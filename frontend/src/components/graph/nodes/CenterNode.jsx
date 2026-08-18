import { Handle, Position } from '@xyflow/react'
import { User } from 'lucide-react'

export function CenterNode({ data }) {
  return (
    <>
      <Handle type="source" position={Position.top} className="opacity-0" />
      <div className="flex flex-col items-center gap-1.5">
        <span className="flex size-14 items-center justify-center rounded-full bg-gradient-brand text-primary-foreground shadow-md">
          <User className="size-6" />
        </span>
        {data.label && (
          <span className="max-w-24 truncate text-xs font-semibold text-foreground">{data.label}</span>
        )}
      </div>
      <Handle type="source" position={Position.bottom} className="opacity-0" />
    </>
  )
}
