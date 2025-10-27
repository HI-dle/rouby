import { getPiniaStorage } from '@/shared/utils/piniaPersistUtils'
import { defineStore } from 'pinia'
import { ref } from 'vue'

// 토큰 저장용 Pinia Store
export const useAuthStore = defineStore(
  'auth',
  () => {
    const staySignedIn = ref(false)
    const accessToken = ref('')
    const refreshToken = ref('')
    const accessTokenExpirationTime = ref(0)

    const setAccessToken = (value) => {
      accessToken.value = value
    }

    const setRefreshToken = (value) => {
      refreshToken.value = value
    }

    const setAccessTokenExpirationTime = (value) => {
      accessTokenExpirationTime.value = value
    }

    const setStaySignedIn = (value) => {
      staySignedIn.value = value
    }

    const getAccessToken = () => {
      return accessToken.value
    }

    const getRefreshToken = () => {
      return refreshToken.value
    }

    const getAccessTokenExpirationTime = () => {
      return accessTokenExpirationTime.value
    }

    const reset = () => {
      staySignedIn.value = false
      accessToken.value = ''
    }

    return {
      staySignedIn,
      accessToken,
      refreshToken,
      accessTokenExpirationTime,

      setAccessToken,
      setRefreshToken,
      setStaySignedIn,
      setAccessTokenExpirationTime,
      getAccessTokenExpirationTime,
      getAccessToken,
      getRefreshToken,
      reset,
    }
  },
  {
    persist: {
      storage: getPiniaStorage(),
    },
  },
)
