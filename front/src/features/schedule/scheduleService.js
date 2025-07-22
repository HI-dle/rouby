import { toCreateSchedulePayload } from './dto'
import { createSchedule as createApi, getSchedules as getApi } from './api'
import { wrapApi } from '@/shared/utils/errorUtils'
import { format } from 'date-fns'

export const createSchedule = wrapApi(async (form) => {
  const payload = toCreateSchedulePayload(form)
  console.log(payload)
  const res = await createApi(payload)
  const scheduleId = res.headers['location']?.split('/').filter(Boolean).pop()
  return { id: scheduleId, ...payload }
}, {})

export const getSchedules = wrapApi(async (fromAt, toAt) => {
  const res = await getApi({
    fromAt: format(fromAt, "yyyy-MM-dd'T'HH:mm:ss"),
    toAt: format(toAt, "yyyy-MM-dd'T'HH:mm:ss"),
  })
  return res
}, {})
