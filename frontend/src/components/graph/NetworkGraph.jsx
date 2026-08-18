import { useMemo } from 'react'
import { ReactFlow, Background, BackgroundVariant } from '@xyflow/react'
import { computeRadialLayout } from '@/components/graph/graphLayout'
import { EntityNode } from '@/components/graph/nodes/EntityNode'
import { CenterNode } from '@/components/graph/nodes/CenterNode'
import { GRAPH_NODE_COLORS } from '@/lib/colors'

const nodeTypes = { entity: EntityNode, center: CenterNode }

/**
 * Radial node-link diagram: a fixed center entity with colleagues/companies/
 * projects/skills arranged around it. `rings` is keyed by node kind, each
 * value a list of { id, label, sublabel? }. Only developer nodes are
 * clickable (they're the only kind with a detail route in this app).
 */
export function NetworkGraph({ center, rings, onDeveloperClick, radius = 200, height = 340 }) {
  const { nodes, edges } = useMemo(() => {
    const laidOut = computeRadialLayout(rings, { radius })

    const flowNodes = [
      {
        id: 'center',
        type: 'center',
        position: { x: 0, y: 0 },
        data: { label: center.label },
        draggable: false,
        selectable: false,
      },
      ...laidOut.map((node) => ({
        id: `${node.kind}:${node.id}`,
        type: 'entity',
        position: node.position,
        data: {
          kind: node.kind,
          label: node.label,
          sublabel: node.sublabel,
          onClick: node.kind === 'developer' ? () => onDeveloperClick?.(node.id) : undefined,
        },
        draggable: false,
      })),
    ]

    const flowEdges = laidOut.map((node) => ({
      id: `edge:${node.kind}:${node.id}`,
      source: 'center',
      target: `${node.kind}:${node.id}`,
      type: 'straight',
      style: { stroke: GRAPH_NODE_COLORS[node.kind], strokeWidth: 1.5, opacity: 0.35 },
    }))

    return { nodes: flowNodes, edges: flowEdges }
  }, [center.label, rings, radius, onDeveloperClick])

  // Ring data (colleagues/companies/projects/skills) arrives from several
  // async queries that resolve after the initial mount. `fitView` only runs
  // once on mount, so without a remount key it frames whatever was present
  // at that moment and later nodes render outside the visible viewport.
  // Keying on the node set forces React Flow to remount and refit whenever
  // it actually changes.
  const layoutKey = nodes.map((node) => node.id).join('|')

  return (
    <div style={{ height }} className="overflow-hidden rounded-xl border border-border bg-muted/30">
      <ReactFlow
        key={layoutKey}
        nodes={nodes}
        edges={edges}
        nodeTypes={nodeTypes}
        fitView
        fitViewOptions={{ padding: 0.35 }}
        proOptions={{ hideAttribution: true }}
        nodesDraggable={false}
        nodesConnectable={false}
        elementsSelectable={false}
        panOnScroll
        zoomOnScroll={false}
      >
        <Background variant={BackgroundVariant.Dots} gap={20} size={1} color="var(--border)" />
      </ReactFlow>
    </div>
  )
}
