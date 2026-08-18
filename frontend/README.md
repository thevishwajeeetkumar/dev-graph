# DevGraph Frontend

Recruiter-facing explorer for the DevGraph talent graph. React 19 + Vite,
plain JavaScript (JSX, no TypeScript), Tailwind CSS v4, shadcn/ui, React
Query, React Router, and React Flow for the node-link graph visualizations.

## Setup

```bash
npm install
cp .env.example .env   # defaults already point at http://localhost:8080
npm run dev
```

The dev server runs on `http://localhost:5173` by default, which is the
origin the backend's CORS config (`APP_CORS_ALLOWED_ORIGINS`) allows out of
the box - no proxy needed as long as the backend is running on `:8080`.

## Backend contract

Every page is built directly against the real backend API - see
`backend/README.md` for the full endpoint list. Nothing here invents
endpoints or response fields; where the wireframe called for data the
backend didn't expose (companies/skills/projects lists, developer
work-history/skills, aggregate stats), the corresponding backend endpoints
were added first (`/api/companies`, `/api/skills`, `/api/projects`,
`/api/developers/{id}/companies`, `/api/developers/{id}/skills`,
`/api/stats/overview`) following the existing controller/service/repository
pattern, and only then wired into the UI.

Scope deliberately excluded: an activity feed, "recent insights", and
month-over-month growth percentages shown in the original wireframe have no
backing data - the graph schema has no timestamps anywhere, so these were
dropped rather than fabricated.

## Structure

```
src/
  lib/          API client (api.js), theme colors (colors.js), React Query client
  hooks/        one React Query hook per backend endpoint
  components/
    ui/         shadcn-generated primitives only
    layout/     app shell, sidebar nav, global search
    combobox/   developer/company pickers (Popover + Command)
    graph/      React Flow radial network visualization
    features/   the 4 power-feature forms + result lists
    developer/  developer detail page building blocks
    dashboard/  overview stat cards, top skills, feature launchers
  pages/        one component per route (see router.jsx)
```

## Verification

With the backend running and seeded (`DEVGRAPH_SEED_ENABLED=true` once,
see `backend/README.md`), try these seeded ids end-to-end:

- Hidden Skills: developer `dev-athompson` -> Kafka via "Payment Gateway Revamp"
- Shortest Path: `dev-athompson` -> `Netflix` -> 2-hop path via Chloe Fischer
- Talent Bridge: `Shopify` vs `Spotify` -> DIRECT bridge via Mia Nakamura
