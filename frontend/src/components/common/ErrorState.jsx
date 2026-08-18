import { AlertTriangle, RotateCcw } from 'lucide-react'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'

export function ErrorState({ error, onRetry }) {
  const message = error?.message ?? 'Something went wrong while talking to the DevGraph API.'

  return (
    <Alert variant="destructive">
      <AlertTriangle className="size-4" />
      <AlertTitle>Couldn&apos;t load this</AlertTitle>
      <AlertDescription>
        <p>{message}</p>
        {onRetry && (
          <Button variant="outline" size="sm" className="mt-3" onClick={onRetry}>
            <RotateCcw className="size-3.5" />
            Retry
          </Button>
        )}
      </AlertDescription>
    </Alert>
  )
}
