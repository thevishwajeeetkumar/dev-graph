import { createBrowserRouter } from 'react-router-dom'
import { AppLayout } from '@/components/layout/AppLayout'
import OverviewPage from '@/pages/OverviewPage'
import DevelopersListPage from '@/pages/DevelopersListPage'
import DeveloperDetailPage from '@/pages/DeveloperDetailPage'
import CompaniesListPage from '@/pages/CompaniesListPage'
import SkillsListPage from '@/pages/SkillsListPage'
import ProjectsListPage from '@/pages/ProjectsListPage'
import ConnectionDiscoveryPage from '@/pages/ConnectionDiscoveryPage'
import ShortestPathPage from '@/pages/ShortestPathPage'
import HiddenSkillsPage from '@/pages/HiddenSkillsPage'
import TalentBridgePage from '@/pages/TalentBridgePage'
import NearestExpertPage from '@/pages/NearestExpertPage'
import AnalyticsPage from '@/pages/AnalyticsPage'
import NotFoundPage from '@/pages/NotFoundPage'

export const router = createBrowserRouter([
  {
    element: <AppLayout />,
    children: [
      { path: '/', element: <OverviewPage /> },
      { path: '/developers', element: <DevelopersListPage /> },
      { path: '/developers/:developerId', element: <DeveloperDetailPage /> },
      { path: '/companies', element: <CompaniesListPage /> },
      { path: '/skills', element: <SkillsListPage /> },
      { path: '/projects', element: <ProjectsListPage /> },
      { path: '/connections', element: <ConnectionDiscoveryPage /> },
      { path: '/shortest-path', element: <ShortestPathPage /> },
      { path: '/hidden-skills', element: <HiddenSkillsPage /> },
      { path: '/talent-bridge', element: <TalentBridgePage /> },
      { path: '/nearest-expert', element: <NearestExpertPage /> },
      { path: '/analytics', element: <AnalyticsPage /> },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
])
