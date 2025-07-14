import { useRoute } from 'vue-router'

export const useActivaPath = () => {
  const route = useRoute()

  const isActive = (path) => route.path.startsWith(path)

  return { isActive }
}
