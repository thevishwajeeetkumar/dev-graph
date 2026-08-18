// Deterministic radial layout: center node at the origin, remaining nodes
// grouped by kind into contiguous angular arcs so same-colored nodes cluster
// visually instead of scattering randomly around the ring.

const KIND_ORDER = ['developer', 'company', 'project', 'skill', 'industry']

export function computeRadialLayout(ringsByKind, { radius = 220 } = {}) {
  const kinds = KIND_ORDER.filter((kind) => ringsByKind[kind]?.length)
  const totalNodes = kinds.reduce((sum, kind) => sum + ringsByKind[kind].length, 0)
  if (totalNodes === 0) return []

  const nodes = []
  let arcStart = 0
  const twoPi = Math.PI * 2

  for (const kind of kinds) {
    const items = ringsByKind[kind]
    const arcSpan = (items.length / totalNodes) * twoPi
    items.forEach((item, index) => {
      const angle = arcStart + ((index + 0.5) / items.length) * arcSpan
      nodes.push({
        ...item,
        kind,
        position: {
          x: Math.round(radius * Math.cos(angle)),
          y: Math.round(radius * Math.sin(angle)),
        },
      })
    })
    arcStart += arcSpan
  }

  return nodes
}
