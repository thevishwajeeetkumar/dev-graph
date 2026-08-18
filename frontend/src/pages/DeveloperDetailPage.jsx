import { useMemo } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import { useDeveloper, useDeveloperCompanies, useDeveloperSkills } from '@/hooks/useDevelopers'
import { useProjects } from '@/hooks/useProjects'
import { useConnections } from '@/hooks/useConnections'
import { DeveloperHeaderCard } from '@/components/developer/DeveloperHeaderCard'
import { DeveloperStatPills } from '@/components/developer/DeveloperStatPills'
import { DeveloperSkillsSection } from '@/components/developer/DeveloperSkillsSection'
import { DeveloperProjectsSection } from '@/components/developer/DeveloperProjectsSection'
import { ConnectionResultsList } from '@/components/features/connections/ConnectionResultsList'
import { HiddenSkillResultsList } from '@/components/features/hidden-skills/HiddenSkillResultsList'
import { NetworkGraph } from '@/components/graph/NetworkGraph'
import { Card, CardContent } from '@/components/ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { ErrorState } from '@/components/common/ErrorState'
import { GRAPH_NODE_COLORS, GRAPH_NODE_LABELS } from '@/lib/colors'

const RING_CAP = 8

export default function DeveloperDetailPage() {
  const { developerId } = useParams()
  const navigate = useNavigate()

  const { data: developer, isPending, isError, error, refetch } = useDeveloper(developerId)
  const { data: companies = [], isPending: companiesPending } = useDeveloperCompanies(developerId)
  const { data: skills = [], isPending: skillsPending } = useDeveloperSkills(developerId)
  const { data: projects = [], isPending: projectsPending } = useProjects(developerId)
  const { data: connectionData } = useConnections(developerId, 0, 25)

  const networkRings = useMemo(() => {
    const colleagues = new Map()
    for (const record of connectionData?.connections ?? []) {
      colleagues.set(record.colleague.id, { id: record.colleague.id, label: record.colleague.name, sublabel: record.colleague.title })
    }
    return {
      developer: Array.from(colleagues.values()).slice(0, RING_CAP),
      company: companies.slice(0, RING_CAP).map((c) => ({ id: c.id, label: c.name })),
      project: projects.slice(0, RING_CAP).map((p) => ({ id: p.id, label: p.name })),
      skill: skills.slice(0, RING_CAP).map((s) => ({ id: s.id, label: s.name })),
    }
  }, [connectionData, companies, projects, skills])

  if (isError) {
    return (
      <div className="mx-auto max-w-3xl space-y-4">
        <Link to="/developers" className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground">
          <ArrowLeft className="size-4" />
          Back to Developers
        </Link>
        <ErrorState error={error} onRetry={refetch} />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-5xl space-y-6">
      <Link to="/developers" className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground">
        <ArrowLeft className="size-4" />
        Back to Developers
      </Link>

      {isPending ? (
        <div className="h-40 animate-pulse rounded-xl bg-muted" />
      ) : (
        <>
          <DeveloperHeaderCard developer={developer} companies={companies} companiesLoading={companiesPending} />
          <DeveloperStatPills
            developerId={developer.id}
            projectsCount={projects.length}
            skillsCount={skills.length}
            companiesCount={companies.length}
            isLoading={projectsPending || skillsPending || companiesPending}
          />

          <Tabs defaultValue="overview">
            <TabsList>
              <TabsTrigger value="overview">Overview</TabsTrigger>
              <TabsTrigger value="projects">Projects ({projects.length})</TabsTrigger>
              <TabsTrigger value="connections">Connections</TabsTrigger>
              <TabsTrigger value="hidden-skills">Hidden Skills</TabsTrigger>
            </TabsList>

            <TabsContent value="overview" className="space-y-6">
              <Card>
                <CardContent className="space-y-4">
                  <div className="flex items-center justify-between">
                    <p className="font-medium text-foreground">Professional Network</p>
                    <div className="flex flex-wrap gap-3 text-xs text-muted-foreground">
                      {Object.entries(GRAPH_NODE_LABELS)
                        .filter(([kind]) => kind !== 'industry')
                        .map(([kind, label]) => (
                          <span key={kind} className="flex items-center gap-1.5">
                            <span
                              className="size-2.5 rounded-full"
                              style={{ backgroundColor: GRAPH_NODE_COLORS[kind] }}
                            />
                            {label}
                          </span>
                        ))}
                    </div>
                  </div>
                  <NetworkGraph
                    center={{ label: developer.name }}
                    rings={networkRings}
                    onDeveloperClick={(id) => navigate(`/developers/${id}`)}
                  />
                </CardContent>
              </Card>

              <div className="grid gap-6 lg:grid-cols-2">
                <Card>
                  <CardContent>
                    <p className="mb-3 font-medium text-foreground">Recent Projects</p>
                    <DeveloperProjectsSection projects={projects.slice(0, 3)} isPending={projectsPending} />
                  </CardContent>
                </Card>
                <Card>
                  <CardContent>
                    <p className="mb-3 font-medium text-foreground">Skills</p>
                    <DeveloperSkillsSection skills={skills} isPending={skillsPending} />
                  </CardContent>
                </Card>
              </div>
            </TabsContent>

            <TabsContent value="projects">
              <DeveloperProjectsSection projects={projects} isPending={projectsPending} />
            </TabsContent>

            <TabsContent value="connections">
              <ConnectionResultsList developerId={developer.id} />
            </TabsContent>

            <TabsContent value="hidden-skills">
              <HiddenSkillResultsList developerId={developer.id} />
            </TabsContent>
          </Tabs>
        </>
      )}
    </div>
  )
}
