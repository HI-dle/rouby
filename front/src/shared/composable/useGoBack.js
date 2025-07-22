import { useRouter } from 'vue-router'

export const useGoBack = () => {
  const router = useRouter()

  const goBackOrPath = async (path = '/') => {
    if (window.history.length > 1) {
      await router.back()
    } else {
      await router.push(path)
    }
  }

  return { goBackOrPath }
}
