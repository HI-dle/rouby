import { wrapApi } from '@/shared/utils/errorUtils.js'
import {
  myPageResetPassword as myPageResetPasswordApi,
  myPageUpdateRoubySetting as myPageUpdateRoubySettingApi,
  myPageReadRoubySetting as myPageReadRoubySettingApi,
} from '@/features/user/api.js'
import { toMyPageResetPasswordPayload, toMyPageRoubySettingPayload } from '@/features/user/dto.js'

export const myPageResetPassword = wrapApi(
  (form) => myPageResetPasswordApi(toMyPageResetPasswordPayload(form)).then(() => ({ ok: true })),
  {
    fieldMessages: {},
    fallbackMessage: '비밀번호 변경에 실패했습니다.',
  },
)

export const updateRoubySetting= wrapApi(
  (form) => myPageUpdateRoubySettingApi(toMyPageRoubySettingPayload(form)).then(() => ({ ok: true })),
  {
    fieldMessages: {
      communicationTone: '말투 입력이 올바르지 않습니다.',
      notificationSettings: '알림 설정이 잘못되었습니다.'
    },
    fallbackMessage: '루비 설정 저장에 실패했습니다.'
  },
)

export const readRoubySetting= wrapApi(
  () => myPageReadRoubySettingApi(),
  {
    fieldMessages: {},
    fallbackMessage: '루비 설정 조회 실패',
  },
)
