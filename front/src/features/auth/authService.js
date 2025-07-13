import { wrapApi } from '@/utils/errorUtils.js'
import {
  requestEmailVerification as requestEmailVerificationApi,
  verifyEmail as verifyEmailApi,
  signup as signupApi,
  myPageResetPassword as myPageResetPasswordApi
} from './api.js'
import {
  toSignupPayload,
  toEmailVerificationPayload,
  toVerifyCodePayload, toMyPageResetPasswordPayload
} from './dto.js'

export const requestEmailVerification = wrapApi(
  (email) => requestEmailVerificationApi(toEmailVerificationPayload(email)),
  {
    targetField: 'email',
    fallbackMessage: '인증 요청에 실패했습니다.',
  },
)

export const verifyEmailCode = wrapApi(
  (email, code) => verifyEmailApi(toVerifyCodePayload(email, code)),
  {
    targetField: 'code',
    fallbackMessage: '인증 코드 검증에 실패했습니다.',
  },
)

export const signup = wrapApi(
  (form) => signupApi(toSignupPayload(form)).then(() => ({ ok: true })),
  {
    fieldMessages: {},
    fallbackMessage: '가입에 실패했습니다.',
  },
)

export const myPageResetPassword = wrapApi(
  (form) => myPageResetPasswordApi(toMyPageResetPasswordPayload(form)).then(() => ({ ok: true })),
  {
    fieldMessages: {},
    fallbackMessage: '비밀번호 변경에 실패했습니다.',
  },
)
