import { RRule } from 'rrule'
import { parseISO, format, startOfMonth, endOfMonth } from 'date-fns'

function createRRule(rrule, dtstart) {
  const { byDay, freq, ...rest } = rrule

  return new RRule({
    ...rest,
    freq: RRule[freq],
    byweekday: byDay ?? undefined,
    dtstart: parseISO(dtstart),
  })
}

function getMonthRange(monthKey) {
  const base = new Date(`${monthKey}-01`)
  return {
    rangeStart: startOfMonth(base),
    rangeEnd: endOfMonth(base),
  }
}

/**
 * 반복 스케줄 + 예외 일정 → 확장된 인스턴스 배열 반환
 * @param schedule 스케줄 객체
 * @param monthKey 'YYYY-MM'
 */
export function expandRecurringSchedule(schedule, monthKey) {
  const recurrence = schedule.recurrenceRule
  const startDate = parseISO(schedule.startAt)
  const { rangeStart, rangeEnd } = getMonthRange(monthKey)

  const overrideMap = new Map(
    (schedule.scheduleOverrides || []).map((o) => [
      format(parseISO(o.overrideDate), 'yyyy-MM-dd'),
      o,
    ]),
  )

  if (!recurrence) {
    const key = format(startDate, 'yyyy-MM-dd')
    return [
      {
        ...schedule,
        originId: schedule.id,
        instanceDate: key,
      },
    ]
  }
  const rule = createRRule(recurrence, recurrence.dtstart || schedule.startAt)
  const dates = rule.between(rangeStart, rangeEnd, true)

  return dates.map((date) => {
    const dateKey = format(date, 'yyyy-MM-dd')
    if (overrideMap.has(dateKey)) {
      return {
        ...overrideMap.get(dateKey),
        originId: schedule.id,
        instanceDate: dateKey,
      }
    } else {
      return {
        ...schedule,
        originId: schedule.id,
        instanceDate: dateKey,
        startAt: date.toISOString(),
        endAt: new Date(
          date.getTime() +
            (new Date(schedule.endAt).getTime() -
              new Date(schedule.startAt).getTime()),
        ).toISOString(),
      }
    }
  })
}

/**
 * 루틴 반복 규칙만 파싱하여 특정 월의 날짜 리스트 반환
 * @param rrule recurrenceRule 객체
 * @param dtstart 시작일 (ISO)
 * @param monthKey 'YYYY-MM'
 * @returns string[] (yyyy-MM-dd)
 */
export function expandRoutineDates(rrule, dtstart, monthKey) {
  const { rangeStart, rangeEnd } = getMonthRange(monthKey)
  const rule = createRRule(rrule, dtstart)

  return rule
    .between(rangeStart, rangeEnd, true)
    .map((d) => format(d, 'yyyy-MM-dd'))
}

export function expandSchedulesByDay(schedules, monthKey) {
  const dailyMap = {}

  schedules.forEach((schedule) => {
    const instances = expandRecurringSchedule(schedule, monthKey)

    instances.forEach((instance) => {
      const dateKey = instance.instanceDate
      const instanceKey = `${instance.originId}@${dateKey}`

      if (!dailyMap[dateKey]) dailyMap[dateKey] = {}

      dailyMap[dateKey][instanceKey] = {
        ...instance,
        time: instance.startAt
          ? new Date(instance.startAt).toISOString().slice(11, 16)
          : null,
      }
    })
  })

  return dailyMap
}
