import { useMemo } from 'react'
import { NetworkGraph } from '@/components/graph/NetworkGraph'

const KIND_LABELS = {
  developer: 'Developers',
  company: 'Companies',
  project: 'Projects',
  skill: 'Skills',
}

/**
 * Decorative preview: density (dot count per kind) is driven by the real
 * /api/stats/overview counts, capped for legibility. Dots are unlabeled and
 * non-interactive - there's no specific "current" entity on the dashboard,
 * so this represents proportions, not identified records.
 */
export function GraphOverviewWidget({ stats }) {
  const rings = useMemo(() => {
    if (!stats) return {}
    const capped = (count) => Math.max(1, Math.min(6, Math.round(Math.sqrt(count))))
    const build = (kind, count) =>
      Array.from({ length: capped(count) }, (_, i) => ({
        id: `${kind}-${i}`,
        kind,
        label: KIND_LABELS[kind],
      }))

    return {
      developer: build('developer', stats.developerCount),
      company: build('company', stats.companyCount),
      project: build('project', stats.projectCount),
      skill: build('skill', stats.skillCount),
    }
  }, [stats])

  return <NetworkGraph center={{ label: 'DevGraph' }} rings={rings} radius={130} height={260} />
}
