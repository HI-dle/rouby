import { toCreateSchedulePayload } from './dto'
import { createSchedule as createApi } from './api'

export const createSchedule = async (form) => {
  const payload = toCreateSchedulePayload(form)

  const res = await createApi(payload)
  const scheduleId = res.headers['location']?.split('/').filter(Boolean).pop()
  return scheduleId
}
