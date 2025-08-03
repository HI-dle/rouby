import { getPiniaStorage } from '@/shared/utils/piniaUtils'
import { defineStore } from 'pinia'
import { ref } from 'vue'

// 토큰 저장용 Pinia Store
export const useAuthStore = defineStore(
  'auth',
  () => {
    const staySignedIn = ref(false)
    const token = ref('')

    const setToken = (value) => {
      token.value = value
    }

    const setStaySignedIn = (value) => {
      staySignedIn.value = value
    }

    const getToken = () => {
      return token.value
    }

    return {
      token,
      staySignedIn,
      setToken,
      setStaySignedIn,
      getToken,
    }
  },
  {
    persist: {
      storage: getPiniaStorage(),
    },
  },
)
