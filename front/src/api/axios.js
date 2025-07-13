import axios from 'axios'
import { getStoredToken } from '@/features/auth/storeToken.js'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 요청 인터셉터 – Authorization 헤더 자동 추가
instance.interceptors.request.use(
  (config) => {
    const token = getStoredToken()
    if (token) {
      config.headers.Authorization = `${token}`
    }
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
