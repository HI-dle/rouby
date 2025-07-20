import { toCreateSchedulePayload } from './dto'
import { createSchedule as createApi, getSchedules as getApi } from './api'
import { wrapApi } from '@/shared/utils/errorUtils'
import { format } from 'date-fns'

export const createSchedule = wrapApi(async (form) => {
  const payload = toCreateSchedulePayload(form)
  const res = await createApi(payload)
  const scheduleId = res.headers['location']?.split('/').filter(Boolean).pop()
  return scheduleId
}, {})

export const getSchedules = wrapApi(async (s, e) => {
  const res = await getApi({
    fromAt: format(s, "yyyy-MM-dd'T'HH:mm:ss"),
    toAt: format(e, "yyyy-MM-dd'T'HH:mm:ss"),
  })
  return res
}, {})
