function describeHop(from, to, relationship) {
  if (relationship === 'WORKED_AT') {
    return `${from.name} currently works at ${to.name}.`
  }
  return `${from.name} works with ${to.name}.`
}

export function PathDetailsList({ nodes, relationshipTypes }) {
  return (
    <div className="space-y-2">
      <p className="text-sm font-medium text-foreground">Path Details</p>
      <ol className="space-y-2">
        {relationshipTypes.map((relationship, index) => (
          <li key={index} className="flex items-start gap-3 text-sm">
            <span className="flex size-5 shrink-0 items-center justify-center rounded-full bg-primary/10 text-xs font-semibold text-primary">
              {index + 1}
            </span>
            <span className="text-foreground/90">
              {describeHop(nodes[index], nodes[index + 1], relationship)}
            </span>
          </li>
        ))}
      </ol>
    </div>
  )
}
