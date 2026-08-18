// Thin typed-in-spirit API client. Every function maps 1:1 to a backend
// endpoint and unwraps the `{ success, data, error, timestamp }` envelope
// every DevGraph response uses (see backend/src/main/java/com/devgraph/dto/ApiResponse.java).

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

export class ApiClientError extends Error {
  constructor(apiError) {
    super(apiError.message)
    this.name = 'ApiClientError'
    this.status = apiError.status
    this.error = apiError.error
  }
}

async function apiFetch(path, params) {
  const url = new URL(path, API_BASE_URL)
  if (params) {
    for (const [key, value] of Object.entries(params)) {
      if (value !== undefined) {
        url.searchParams.set(key, String(value))
      }
    }
  }

  let response
  try {
    response = await fetch(url)
  } catch {
    throw new ApiClientError({
      status: 0,
      error: 'Network Error',
      message: 'Could not reach the DevGraph API. Check that the backend is running.',
    })
  }

  let json
  try {
    json = await response.json()
  } catch {
    throw new ApiClientError({
      status: response.status,
      error: 'Invalid Response',
      message: 'The server returned an unreadable response.',
    })
  }

  if (!json.success || json.data === null) {
    throw new ApiClientError(
      json.error ?? {
        status: response.status,
        error: 'Unknown Error',
        message: 'The request failed for an unknown reason.',
      },
    )
  }

  return json.data
}

export const getDevelopers = () => apiFetch('/api/developers')

export const getDeveloper = (developerId) => apiFetch(`/api/developers/${developerId}`)

export const getDeveloperCompanies = (developerId) =>
  apiFetch(`/api/developers/${developerId}/companies`)

export const getDeveloperSkills = (developerId) =>
  apiFetch(`/api/developers/${developerId}/skills`)

export const getCompanies = () => apiFetch('/api/companies')

export const getSkills = () => apiFetch('/api/skills')

export const getProjects = (developerId) => apiFetch('/api/projects', { developerId })

export const getOverviewStats = (topSkillsLimit) =>
  apiFetch('/api/stats/overview', { topSkillsLimit })

export const getConnections = (developerId, offset, limit) =>
  apiFetch('/api/graph/connections', { developerId, offset, limit })

export const getShortestPath = (developerId, companyId, maxHops) =>
  apiFetch('/api/graph/shortest-path', { developerId, companyId, maxHops })

export const getHiddenSkills = (developerId, offset, limit) =>
  apiFetch('/api/graph/hidden-skills', { developerId, offset, limit })

export const getTalentBridges = (companyAId, companyBId, offset, limit) =>
  apiFetch('/api/graph/talent-bridge', { companyAId, companyBId, offset, limit })

export const getNearestExpert = (developerId, skillId, maxHops) =>
  apiFetch('/api/graph/nearest-expert', { developerId, skillId, maxHops })
