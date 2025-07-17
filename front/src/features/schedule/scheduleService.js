import { toCreateSchedulePayload } from './dto'
import { createSchedule as createApi } from './api'
import { wrapApi } from '@/shared/utils/errorUtils'

export const createSchedule = wrapApi(async (form) => {
  const payload = toCreateSchedulePayload(form)
  const res = await createApi(payload)
  const scheduleId = res.headers['location']?.split('/').filter(Boolean).pop()
  return scheduleId
}, {})
