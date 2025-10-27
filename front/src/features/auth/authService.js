import { wrapApi } from '@/shared/utils/errorUtils.js'
import {
  findPassword as findPasswordApi,
  getUserBasicInfo,
  login,
  requestEmailVerification as requestEmailVerificationApi,
  resetPassword as resetPasswordApi,
  refresh as refreshApi,
  signup as signupApi,
  verificationPasswordCode as verificationPasswordCodeApi,
  verifyEmail as verifyEmailApi,
} from './api.js'
import {
  toEmailVerificationPayload, toRefreshPayload,
  toResetPasswordPayload,
  toSignupPayload,
  toVerifyCodePayload,
  toVerifyPasswordCodePayload
} from './dto.js'
import { useUserInfoStore } from '@/stores/useUserInfoStore.js'
import { useAuthStore } from '@/stores/useAuthStore.js'
import { registerUserDevice } from '../user/userService.js'
import { requestPermissionAndInitFCM } from '@/shared/utils/notificationUtils.js'
import { setPiniaStorage } from '@/shared/utils/piniaPersistUtils.js'
import { getTokenExpiration } from '@/shared/utils/jwtUtils'

export const requestEmailVerification = wrapApi(
  (email) => requestEmailVerificationApi(toEmailVerificationPayload(email)),
  {
    targetField: 'email',
    fallbackMessage: '인증 요청에 실패했습니다.',
  },
)

export const verifyEmailCode = wrapApi(
  (email, code) => {
    return verifyEmailApi(toVerifyCodePayload(email, code))
  },
  {
    targetField: 'code',
    fallbackMessage: '인증 코드 검증에 실패했습니다.',
  },
)

export const signup = wrapApi(
  async (form) => {
    const res = await signupApi(toSignupPayload(form), form.verificationToken)
    return {
      data: {
        ok: res.status === 201,
      },
    }
  },
  {
    fallbackMessage: '가입에 실패했습니다.',
  },
)

export const findPassword = wrapApi(
  (email) => findPasswordApi(toEmailVerificationPayload(email)),
  {
    targetField: 'email',
    fallbackMessage: '비밀번호 재설정 링크 전송 요청에 실패했습니다.',
  },
)

export const resetPassword = wrapApi(
  (form) =>
    resetPasswordApi(toResetPasswordPayload(form)).then(() => ({ ok: true })),
  {
    fieldMessages: {},
    fallbackMessage: '비밀번호 변경에 실패했습니다.',
  },
)

export const verificationPasswordCode = wrapApi(
  (form) =>
    verificationPasswordCodeApi(toVerifyPasswordCodePayload(form)).then(() => ({
      ok: true,
    })),
  {
    targetField: 'code',
    fallbackMessage: '인증 코드 검증에 실패했습니다.',
  },
)

export const refresh = wrapApi(
  async (form) => {
    const response = await refreshApi(toRefreshPayload(form))
    const { accessToken, refreshToken } = response.data
    return {
      ok: true,
      accessToken,
      refreshToken,
    }
  },
  {
    fieldMessages: {},
    fallbackMessage: '토큰 갱신에 실패했습니다.',
  },
)

export const loginAndBootstrap = async (email, password, staySignedIn) => {
  const userInfoStore = useUserInfoStore()
  const authStore = useAuthStore()

  // 1. 로그인 요청
  const response = await login({
    email: email.trim(),
    password: password,
  })

  // 2. 토큰 저장
  setPiniaStorage(staySignedIn)
  authStore.setAccessToken(response.data.accessToken)
  authStore.setRefreshToken(response.data.refreshToken)
  authStore.setStaySignedIn(staySignedIn)
  authStore.setAccessTokenExpirationTime(
    getTokenExpiration(response.data.accessToken)
  )

  // 3. 유저 기본 정보 요청
  const userRes = await getUserBasicInfo()
  const user = userRes.data

  userInfoStore.setUserInfoWithDefaults({ ...user })

  // 4. 알림 퍼미션 확인 및 사용자 토큰 정보 등록
  await requestPermissionAndInitFCM()
  await registerUserDevice()

  // 5. 라우팅 정보
  const nextPath = user.onboardingStatePath
  return nextPath
}
