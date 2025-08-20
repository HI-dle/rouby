import { useRouter } from 'vue-router'

export const useGoBack = () => {
  const router = useRouter()

  const goBackOrPath = async (path = '/') => {
    if (window.history.length > 1) {
      router.back()
    } else {
      await router.push(path)
    }
  }

  const goPathOrBack = async (path = '/') => {
    if (path != '/') {
      await router.push(path)
    } else if (window.history.length > 1) {
      router.back()
    } else {
      await router.push(path)
    }
  }

  return { goBackOrPath, goPathOrBack }
}
