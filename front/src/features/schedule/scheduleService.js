import { toCreateSchedulePayload, toUpdateSchedulePayload } from './dto'
import { createSchedule as createApi, getSchedules as getApi, updateSchedule as updateApi } from './api'
import { deleteSchedule as deleteApi, deleteSchedulesStartingFrom as deleteFromApi } from './api'
import { wrapApi } from '@/shared/utils/errorUtils'
import { format } from 'date-fns'

export const createSchedule = wrapApi(async (form) => {
  const payload = toCreateSchedulePayload(form)
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

export const updateSchedule = wrapApi(async (form, dailySchedule) => {
  const payload = toUpdateSchedulePayload(form, dailySchedule)

  const res = await updateApi(payload)

  return { ...payload, id: res.data }
}, {})

export const deleteSchedule = wrapApi(async ({ scheduleId, instanceDate, startAt, endAt }) => {
  await deleteApi({ scheduleId, instanceDate, startAt, endAt })
}, {})

export const deleteSchedulesStartingFrom = wrapApi(async ({ scheduleId, fromAt }) => {
  await deleteFromApi({ scheduleId, fromAt })
}, {})
