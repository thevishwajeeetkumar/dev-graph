import { Outlet } from 'react-router-dom'
import { SidebarProvider, SidebarInset, SidebarTrigger } from '@/components/ui/sidebar'
import { TooltipProvider } from '@/components/ui/tooltip'
import { AppSidebar } from '@/components/layout/AppSidebar'

export function AppLayout() {
  return (
    <TooltipProvider delayDuration={150}>
      <SidebarProvider>
        <AppSidebar />
        <SidebarInset className="bg-background">
          <div className="flex items-center gap-2 border-b border-border px-4 py-3 md:hidden">
            <SidebarTrigger />
            <span className="text-sm font-medium text-foreground">DevGraph</span>
          </div>
          <main className="flex-1 p-4 md:p-8">
            <Outlet />
          </main>
        </SidebarInset>
      </SidebarProvider>
    </TooltipProvider>
  )
}
