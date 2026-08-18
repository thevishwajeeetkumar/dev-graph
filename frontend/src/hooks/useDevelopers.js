import { useQuery } from '@tanstack/react-query'
import { getDeveloper, getDeveloperCompanies, getDeveloperSkills, getDevelopers } from '@/lib/api'

export function useDevelopers() {
  return useQuery({
    queryKey: ['developers'],
    queryFn: getDevelopers,
  })
}

export function useDeveloper(developerId) {
  return useQuery({
    queryKey: ['developers', developerId],
    queryFn: () => getDeveloper(developerId),
    enabled: Boolean(developerId),
  })
}

export function useDeveloperCompanies(developerId) {
  return useQuery({
    queryKey: ['developers', developerId, 'companies'],
    queryFn: () => getDeveloperCompanies(developerId),
    enabled: Boolean(developerId),
  })
}

export function useDeveloperSkills(developerId) {
  return useQuery({
    queryKey: ['developers', developerId, 'skills'],
    queryFn: () => getDeveloperSkills(developerId),
    enabled: Boolean(developerId),
  })
}
