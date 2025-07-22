import ICAL from 'ical.js'
import {
  parseISO,
  format,
  startOfMonth,
  endOfMonth,
  isAfter,
  isSameDay,
  startOfDay,
} from 'date-fns'

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

function expandMultiDaySchedule(base, start, end, rangeEnd) {
  const result = []

  for (
    let d = startOfDay(new Date(start));
    d < end;
    d.setDate(d.getDate() + 1)
  ) {
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

export function expandRecurringSchedule(schedule, monthKey) {
  const recurrence = schedule.recurrenceRule
  const startDate = parseISO(schedule.startAt)
  const endDate = parseISO(schedule.endAt)
  const durationMs = endDate.getTime() - startDate.getTime()
  const { rangeStart, rangeEnd } = getMonthRange(monthKey)

  const overrideMap = new Map(
    (schedule.scheduleOverrides || []).map((o) => [
      format(parseISO(o.overrideDate), 'yyyy-MM-dd'),
      o,
    ]),
  )

  const result = []

  if (!recurrence || !recurrence.rruleStr) {
    return expandMultiDaySchedule(
      {
        ...schedule,
        originId: schedule.id,
      },
      startDate,
      endDate,
      rangeEnd,
    )
  }

  const event = createIcalComponent(
    recurrence.rruleStr,
    recurrence.dtstart || schedule.startAt,
  )
  const iterator = event.iterator()

  let next
  while ((next = iterator.next())) {
    const nextDate = next.toJSDate()
    if (nextDate > rangeEnd) break
    if (nextDate < rangeStart) continue

    const dateKey = format(nextDate, 'yyyy-MM-dd')

    if (overrideMap.has(dateKey)) {
      const override = overrideMap.get(dateKey)
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
          rangeEnd,
        ),
      )
    } else {
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
          rangeEnd,
        ),
      )
    }
  }

  return result
}

export function expandSchedulesByDay(schedules, monthKey) {
  const dailyMap = {}
  const rawMap = {}

  schedules.forEach((schedule) => {
    rawMap[schedule.id] = schedule

    const instances = expandRecurringSchedule(schedule, monthKey)

    instances.forEach((instance) => {
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
