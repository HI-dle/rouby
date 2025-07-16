import {wrapApi} from '@/shared/utils/errorUtils.js'
import {
  findPassword as findPasswordApi,
  requestEmailVerification as requestEmailVerificationApi,
  resetPassword as resetPasswordApi,
  signup as signupApi,
  verificationPasswordCode as verificationPasswordCodeApi,
  verifyEmail as verifyEmailApi,
} from './api.js'
import {
  toEmailVerificationPayload,
  toResetPasswordPayload,
  toSignupPayload,
  toVerifyCodePayload,
  toVerifyPasswordCodePayload
} from './dto.js'

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
