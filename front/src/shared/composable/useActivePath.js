import { useRoute } from 'vue-router'

export const useActivePath = () => {
  const route = useRoute()

  const isActive = (path) => route.path.startsWith(path)

  return { isActive }
}
