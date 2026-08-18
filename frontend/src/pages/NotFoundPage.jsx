import { Link } from 'react-router-dom'
import { Compass } from 'lucide-react'
import { Button } from '@/components/ui/button'

export default function NotFoundPage() {
  return (
    <div className="mx-auto flex max-w-md flex-col items-center gap-4 py-24 text-center">
      <span className="flex size-14 items-center justify-center rounded-full bg-muted text-muted-foreground">
        <Compass className="size-6" />
      </span>
      <div>
        <h1 className="text-xl font-semibold text-foreground">Page not found</h1>
        <p className="mt-1 text-sm text-muted-foreground">
          The page you're looking for doesn't exist.
        </p>
      </div>
      <Button asChild className="bg-primary hover:bg-primary-hover">
        <Link to="/">Back to Overview</Link>
      </Button>
    </div>
  )
}
