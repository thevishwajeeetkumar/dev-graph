import { NavLink } from 'react-router-dom'
import {
  Building2,
  GitBranch,
  LayoutDashboard,
  LineChart,
  Share2,
  Sparkles,
  Users,
  Waypoints,
} from 'lucide-react'
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'

const NAV_ITEMS = [
  { to: '/', label: 'Overview', icon: LayoutDashboard, end: true },
  { to: '/developers', label: 'Developers', icon: Users },
  { to: '/companies', label: 'Companies', icon: Building2 },
  { to: '/skills', label: 'Skills', icon: Sparkles },
  { to: '/projects', label: 'Projects', icon: GitBranch },
  { to: '/connections', label: 'Connections', icon: Share2 },
  { to: '/analytics', label: 'Analytics', icon: LineChart },
]

export function AppSidebar() {
  return (
    <Sidebar collapsible="offcanvas">
      <SidebarHeader className="px-3 py-4">
        <div className="flex items-center gap-2 px-1">
          <div className="flex size-8 items-center justify-center rounded-lg bg-gradient-brand text-primary-foreground">
            <Waypoints className="size-4.5" />
          </div>
          <span className="text-base font-semibold tracking-tight text-foreground">DevGraph</span>
        </div>
      </SidebarHeader>
      <SidebarContent className="px-2">
        <SidebarGroup>
          <SidebarGroupContent>
            <SidebarMenu>
              {NAV_ITEMS.map((item) => (
                <SidebarMenuItem key={item.to}>
                  <SidebarMenuButton asChild>
                    <NavLink
                      to={item.to}
                      end={item.end}
                      className={({ isActive }) =>
                        isActive
                          ? 'bg-sidebar-accent font-medium text-primary'
                          : 'text-sidebar-foreground'
                      }
                    >
                      <item.icon className="size-4" />
                      <span>{item.label}</span>
                    </NavLink>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter className="px-3 py-3">
        <div className="flex items-center gap-2.5 rounded-xl border border-sidebar-border bg-sidebar-accent/60 px-2.5 py-2">
          <Avatar className="size-8">
            <AvatarFallback className="bg-gradient-brand text-xs font-medium text-primary-foreground">
              SJ
            </AvatarFallback>
          </Avatar>
          <div className="min-w-0">
            <p className="truncate text-sm font-medium text-foreground">Sarah Johnson</p>
            <p className="truncate text-xs text-muted-foreground">Recruiter</p>
          </div>
        </div>
      </SidebarFooter>
    </Sidebar>
  )
}
