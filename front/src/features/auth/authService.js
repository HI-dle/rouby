import { wrapApi } from '@/shared/utils/errorUtils.js'
import {
  requestEmailVerification as requestEmailVerificationApi,
  verifyEmail as verifyEmailApi,
  signup as signupApi,
} from './api.js'
import {
  toSignupPayload,
  toEmailVerificationPayload,
  toVerifyCodePayload
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
  async (form) => {
    try {
      const res = await signupApi(toSignupPayload(form))

      return {
        data: {
          ok: res.status === 201
        }
      }
    } catch (error) {
      throw error
    }
  },
  {
    fieldMessages: {},
    fallbackMessage: '가입에 실패했습니다.',
  },
)
