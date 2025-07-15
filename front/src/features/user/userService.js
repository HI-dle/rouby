import { wrapApi } from '@/utils/errorUtils.js'
import { myPageResetPassword as myPageResetPasswordApi } from '@/features/user/api.js'
import { toMyPageResetPasswordPayload } from '@/features/user/dto.js'

export const myPageResetPassword = wrapApi(
  (form) => myPageResetPasswordApi(toMyPageResetPasswordPayload(form)).then(() => ({ ok: true })),
  {
    fieldMessages: {},
    fallbackMessage: '비밀번호 변경에 실패했습니다.',
  },
)
