import {
  convertDateToDateTime,
  extractDate,
  formatDateTime,
} from '@/shared/utils/dateTimeUtils'
import { BIWEEKLY, BYDAY, WEEKELY } from './constants'

const getWeekdays = (from, end) => {
  const startDate = new Date(from)
  const endDate = new Date(end)
  const includedWeekdays = new Set()

  for (let d = new Date(startDate); d <= endDate; d.setDate(d.getDate() + 1)) {
    includedWeekdays.add(d.getDay())
  }
  if (includedWeekdays.size === 7) return [] // all days covered

  return [...includedWeekdays].map((d) => BYDAY[d])
}

const getNxtDate = (dateStr) => {
  const date = new Date(dateStr)
  date.setDate(date.getDate() + 1)

  return formatDateTime(date)
}

export function toCreateSchedulePayload(form) {
  if (!form || typeof form !== 'object') {
    throw new Error('유효하지 않은 form 객체입니다.')
  }

  const weekdays = getWeekdays(form.start, form.end)

  return {
    title: form.title,
    memo: form.memo,
    alarmOffsetMinutes: form.alarmOffsetMinutes,
    routineStart: extractDate(form.routineStart),
    startAt: form.allDay
      ? convertDateToDateTime(extractDate(form.start), 0)
      : formatDateTime(form.start),
    endAt: form.allDay
      ? convertDateToDateTime(extractDate(getNxtDate(form.end)), 0)
      : formatDateTime(form.end),
    recurrenceRule: !form.repeat
      ? null
      : {
          freq: form.repeat !== BIWEEKLY ? form.repeat : WEEKELY,
          byDay: weekdays.length < 1 ? null : weekdays.map((d) => d).join(','),
          interval: !form.repeat ? null : form.repeat !== BIWEEKLY ? 1 : 2,
          until: null,
        },
  }
}
