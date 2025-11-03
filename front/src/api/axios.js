import { useAuthStore } from '@/stores/useAuthStore'
import axios from 'axios'
import { refresh } from '@/features/auth/authService.js'
import { decodeJwt } from '@/shared/utils/jwtUtils'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

let isRefreshing = false
let refreshSubscribers = []

function onRefreshed(newAccessToken) {
  refreshSubscribers.forEach(({ resolve }) => resolve(newAccessToken))
  refreshSubscribers = []
}

function onRefreshFailed(error) {
  refreshSubscribers.forEach(({ reject }) => reject(error))
  refreshSubscribers = []
}

function addRefreshSubscriber(handlers) {
  refreshSubscribers.push(handlers)
}

async function refreshAccessToken(authStore) {
  try {
    const { accessToken: newAccessToken, refreshToken: newRefreshToken } = await refresh({
      refreshToken: authStore.getRefreshToken(),
    })
    const decoded = decodeJwt(newAccessToken)
    const expiredAt = decoded?.exp ? decoded.exp * 1000 : null

    // 새 토큰 및 만료 시간 저장
    authStore.setAccessToken(newAccessToken)
    authStore.setAccessTokenExpirationTime(expiredAt)
    if (newRefreshToken) {
      authStore.setRefreshToken(newRefreshToken)
    }

    console.info('AccessToken 갱신 성공')
    onRefreshed(newAccessToken)
    return newAccessToken
  } catch (error) {
    console.error('Refresh 실패:', error)
    onRefreshFailed(error)
    authStore.reset()
    window.location.href = '/login'
    throw error
  } finally {
    isRefreshing = false
  }
}

// 요청 인터셉터 – Authorization 헤더 자동 추가
instance.interceptors.request.use(
  async (config) => {
    const authStore = useAuthStore()

    if (config.url.includes('/auth/refresh')) return config

    const token = authStore.getAccessToken()
    const expiredAt = authStore.getAccessTokenExpirationTime()

    if (!token) return config
    if (config.headers.Authorization) return config

    const now = Date.now()

    // 만료된 토큰이면 refresh 시도
    if (expiredAt && now >= expiredAt) {
      console.warn('AccessToken 만료 → refresh 요청 시작')

      // 이미 갱신 중이라면 기다림
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          addRefreshSubscriber({ resolve, reject })
        }).then((newAccessToken) => {
          config.headers.Authorization = `Bearer ${newAccessToken}`
          return config
        })
      }

      // refresh 수행
      isRefreshing = true
      try {
        const newAccessToken = await refreshAccessToken(authStore)
        config.headers.Authorization = `Bearer ${newAccessToken}`
        return config
      } catch (error) {
        return Promise.reject(error)
      }
    }

    // 만료되지 않았으면 그대로 요청
    config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error),
)

// 응답 인터셉터 – 공통 에러 처리 등
instance.interceptors.response.use(
  (response) => response,
  (error) => {
    // 예: 토큰 만료시 로그아웃 처리
    if (error.response?.status === 401) {
      // 예: router.push('/login')
    }
    return Promise.reject(error)
  },
)

export default instance
