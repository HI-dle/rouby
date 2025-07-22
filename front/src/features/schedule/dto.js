import {
  convertDateToDateTime,
  extractDate,
  formatDateTime,
} from '@/shared/utils/dateTimeUtils'
import { BIWEEKLY, BYDAY, WEEKELY } from './constants'
import { getNxtDate } from '@/shared/utils/dateUtils'
import { subDays } from 'date-fns'

export function toCreateSchedulePayload(form) {
  if (!form || typeof form !== 'object') {
    throw new Error('유효하지 않은 form 객체입니다.')
  }

  return {
    title: form.title,
    memo: form.memo,
    alarmOffsetMinutes: form.alarmOffsetMinutes,
    routineOffsetDays: subDays(form.routineStart, form.start),
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
          byDay: null,
          interval: !form.repeat ? null : form.repeat !== BIWEEKLY ? 1 : 2,
          until: null,
        },
  }
}
