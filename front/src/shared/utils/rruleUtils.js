import ICAL from 'ical.js'
import {
  parseISO,
  format,
  startOfMonth,
  endOfMonth,
  isAfter,
  isSameDay,
  startOfDay,
  isBefore,
} from 'date-fns'
import { isAllDay } from './dateUtils'

function getMonthRange(monthKey) {
  const base = new Date(`${monthKey}-01`)
  return {
    rangeStart: startOfMonth(base),
    rangeEnd: endOfMonth(base),
  }
}

function createIcalComponent(rruleStr, dtstart) {
  const vevent = new ICAL.Component('vevent')
  const event = new ICAL.Event(vevent)

  event.startDate = ICAL.Time.fromJSDate(new Date(dtstart), false)
  event.component.addPropertyWithValue('rrule', ICAL.Recur.fromString(rruleStr))

  return event
}

function expandMultiDaySchedule(base, start, end, rangeStart, rangeEnd) {
  const result = []

  let startDate = new Date(start)
  if (isBefore(startDate, rangeStart)) startDate = rangeStart

  for (let d = startOfDay(startDate); d < end; d.setDate(d.getDate() + 1)) {
    const dCopy = new Date(d)
    if (isAfter(dCopy, rangeEnd)) break

    const dKey = format(dCopy, 'yyyy-MM-dd')
    result.push({
      ...base,
      instanceDate: dKey,
      startAt: format(start, "yyyy-MM-dd'T'HH:mm:ss"),
      endAt: format(end, "yyyy-MM-dd'T'HH:mm:ss"),
      isContinued: !isSameDay(dCopy, start),
    })
  }

  return result
}

function expandRecurringSchedule(schedule, monthKey) {
  const recurrence = schedule.recurrenceRule
  const startDate = parseISO(schedule.startAt)
  const endDate = parseISO(schedule.endAt)
  const durationMs = endDate.getTime() - startDate.getTime()
  const { rangeStart, rangeEnd } = getMonthRange(monthKey)

  // 반복이 없는 경우
  if (!recurrence || !recurrence.rruleStr) {
    return expandMultiDaySchedule(
      {
        ...schedule,
        originId: schedule.id,
      },
      startDate,
      endDate,
      rangeStart,
      rangeEnd,
    )
  }

  // 반복이 있는 경우
  const overrideDates = new Set(
    (schedule.scheduleOverrides || [])
    .map(o => format(parseISO(o.overrideDate), 'yyyy-MM-dd'))
  )


  const event = createIcalComponent(
    recurrence.rruleStr,
    recurrence.dtstart || schedule.startAt,
  )
  const iterator = event.iterator(ICAL.Time.fromJSDate(rangeStart, false))

  const result = []
  let next
  while ((next = iterator.next())) {
    const nextDate = next.toJSDate()
    if (nextDate > rangeEnd) break

    const dateKey = format(nextDate, 'yyyy-MM-dd')

    if (overrideDates.has(dateKey)) continue // 오버라이드는 아래 따로 추가

    const start = new Date(nextDate)
    const end = new Date(start.getTime() + durationMs)

    result.push(
      ...expandMultiDaySchedule(
        {
          ...schedule,
          originId: schedule.id,
        },
        start,
        end,
        rangeStart,
        rangeEnd,
      ),
    )
  }

  for (const override of schedule.scheduleOverrides || []) {
    if (override.overrideType?.toUpperCase() === 'CANCELLED') continue
    const oStart = parseISO(override.startAt)
    const oEnd = parseISO(override.endAt)

    result.push(
      ...expandMultiDaySchedule(
        {
          ...override,
          originId: schedule.id,
        },
        oStart,
        oEnd,
        rangeStart,
        rangeEnd,
      ),
    )
  }
  return result
}

const compareSchedule = (a, b) => {
  // 1) 여러날 일정 우선
  if (a.isContinued !== b.isContinued) return a.isContinued ? -1 : 1

  // 2) 하루종일(>=24h) 우선
  const aAll = isAllDay(a.startAt, a.endAt)
  const bAll = isAllDay(b.startAt, b.endAt)
  if (aAll !== bAll) return aAll ? -1 : 1

  // 3) 시작시간 오름차순
  const as = new Date(a.startAt)
  const bs = new Date(b.startAt)
  if (as && bs) {
    if (isBefore(as, bs)) return -1
    if (isBefore(bs, as)) return 1
  } else if (as || bs) {
    // 시작시간 없는 건 뒤로
    return as ? -1 : 1
  }

  // 4) 동률이면 종료시간 → 제목 → id로 안정적 타이브레이크
  const ae = new Date(a.endAt)
  const be = new Date(b.endAt)
  if (ae && be) {
    if (isBefore(ae, be)) return -1
    if (isBefore(be, ae)) return 1
  } else if (ae || be) {
    return ae ? -1 : 1
  }

  return (
    String(a.title ?? '').localeCompare(String(b.title ?? '')) ||
    String(a.id ?? '').localeCompare(String(b.id ?? ''))
  )
}

export function expandSchedulesByDay(schedules, monthKey) {
  const dailyMap = {}
  const rawMap = {}

  schedules.forEach((schedule) => {
    rawMap[schedule.id] = schedule

    const instances = expandRecurringSchedule(schedule, monthKey)

    instances.sort(compareSchedule).forEach((instance) => {
      const dateKey = instance.instanceDate
      const instanceKey = `${instance.id}@${dateKey}`

      if (!dailyMap[dateKey]) dailyMap[dateKey] = {}

      dailyMap[dateKey][instanceKey] = {
        ...instance,
      }
    })
  })

  return { dailyMap, rawMap }
}

export function expandRoutineDates(rrule, dtstart, monthKey) {
  const { rangeStart, rangeEnd } = getMonthRange(monthKey)
  const event = createIcalComponent(rrule, dtstart)
  const iterator = event.iterator()

  const result = []
  let next
  while ((next = iterator.next())) {
    const date = next.toJSDate()
    if (date > rangeEnd) break
    if (date >= rangeStart) {
      result.push(format(date, 'yyyy-MM-dd'))
    }
  }

  return result
}

export function buildRRuleString(recurrenceRule) {
  const parts = []

  if (!recurrenceRule || !recurrenceRule.freq) {
    return null
  }

  parts.push(`FREQ=${recurrenceRule.freq}`)

  if (recurrenceRule.interval && recurrenceRule.interval > 1) {
    parts.push(`INTERVAL=${recurrenceRule.interval}`)
  }

  if (Array.isArray(recurrenceRule.byDay) && recurrenceRule.byDay.length > 0) {
    parts.push(`BYDAY=${recurrenceRule.byDay.join(',')}`)
  }

  if (recurrenceRule.until) {
    const untilDate = new Date(recurrenceRule.until)
    const formattedUntil = format(untilDate, "yyyyMMdd'T'HHmmss'Z'")
    parts.push(`UNTIL=${formattedUntil}`)
  }

  if (recurrenceRule.count) {
    parts.push(`COUNT=${recurrenceRule.count}`)
  }

  return parts.join(';')
}
