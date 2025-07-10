import { useRouter } from 'vue-router'

export const useGoBack = (path = '/') => {
  const router = useRouter()

  const goBackOrPath = (path = '/') => {
    if (window.history.length > 1) {
      router.back()
    } else {
      router.push(path)
    }
  }

  return { goBackOrPath }
}
