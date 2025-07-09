export function toCreateSchedulePayload(form) {
  return {
    title: form.title,
    memo: form.memo,
    start: form.start,
    end: form.end,
    allDay: form.allDay,
    alarmOffsetMinutes: form.alarmOffsetMinutes,
    repeat: form.repeat,
    routineStart: form.routineStart,
  }
}
