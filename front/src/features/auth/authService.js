import { wrapApi } from '@/utils/errorUtils.js'
import {
  requestEmailVerification as requestEmailVerificationApi,
  verifyEmail as verifyEmailApi,
  signup as signupApi,
  findPassword as findPasswordApi,
  resetPassword as resetPasswordApi,
  verificationPasswordCode as verificationPasswordCodeApi,
} from './api.js'
import {
  toSignupPayload,
  toEmailVerificationPayload,
  toVerifyCodePayload,
  toVerifyPasswordCodePayload,
  toResetPasswordPayload
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

export const findPassword = wrapApi(
  (email) => findPasswordApi(toEmailVerificationPayload(email)),
  {
    targetField: 'email',
    fallbackMessage: '비밀번호 재설정 링크 전송 요청에 실패했습니다.',
  },
)

export const resetPassword = wrapApi(
  (form) => resetPasswordApi(toResetPasswordPayload(form)).then(() => ({ ok: true })),
  {
    fieldMessages: {},
    fallbackMessage: '비밀번호 변경에 실패했습니다.',
  },
)

export const verificationPasswordCode = wrapApi(
  (form) => verificationPasswordCodeApi(toVerifyPasswordCodePayload(form)).then(() => ({ ok: true })),
  {
    targetField: 'code',
    fallbackMessage: '인증 코드 검증에 실패했습니다.',
  },
)
