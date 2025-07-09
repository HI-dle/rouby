import { toCreateSchedulePayload } from './dto'
import { createSchedule as createApi } from './api'

export const createSchedule = async (form) => {
  const payload = toCreateSchedulePayload(form)

  try {
    const res = await createApi(payload)
  } catch (err) {
    const msg = err.response?.data?.message || err.message || '저장 실패'
    alert(`저장에 실패하였습니다.\n${msg}`)
    return null
  }
}
