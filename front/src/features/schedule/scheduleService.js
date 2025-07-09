import { toCreateSchedulePayload } from './dto'
import { createSchedule as createApi } from './api'
import { useGoBack } from '@/shared/composable/useGoBack'

const { goBack } = useGoBack()

export const createSchedule = async (form) => {
  const payload = toCreateSchedulePayload(form)

  try {
    const res = await createApi(payload)
    const location = res.headers['location']
    const lastSegment = location.split('/').filter(Boolean).pop()
    goBack(`/schedule/detail/${lastSegment}`)
  } catch (err) {
    const msg = err.response?.data?.message || err.message || '저장 실패'
    alert(`저장에 실패하였습니다.\n${msg}`)
  }
}
