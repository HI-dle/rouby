import ICAL from 'ical.js'
import {
  endOfMonth,
  format,
  isAfter,
  isBefore,
  isSameDay,
  parseISO,
  startOfDay,
  startOfMonth,
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
  if (isBefore(startDate, rangeStart)) {
    startDate = rangeStart
  }

  for (let d = startOfDay(startDate); d < end; d.setDate(d.getDate() + 1)) {
    const dCopy = new Date(d)
    if (isAfter(dCopy, rangeEnd)) {
      break
    }

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
    (schedule.scheduleOverrides || []).map((o) =>
      format(parseISO(o.overrideDate), 'yyyy-MM-dd'),
    ),
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
    if (nextDate > rangeEnd) {
      break
    }

    const dateKey = format(nextDate, 'yyyy-MM-dd')

    if (overrideDates.has(dateKey)) {
      continue
    } // 오버라이드는 아래 따로 추가

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

  // 오버라이드 된 일정 추가
  for (const override of schedule.scheduleOverrides || []) {
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
  if (a.isContinued !== b.isContinued) {
    return a.isContinued ? -1 : 1
  }

  // 2) 하루종일(>=24h) 우선
  const aAll = isAllDay(a.startAt, a.endAt)
  const bAll = isAllDay(b.startAt, b.endAt)
  if (aAll !== bAll) {
    return aAll ? -1 : 1
  }

  // 3) 시작시간 오름차순
  const as = new Date(a.startAt)
  const bs = new Date(b.startAt)
  if (as && bs) {
    if (isBefore(as, bs)) {
      return -1
    }
    if (isBefore(bs, as)) {
      return 1
    }
  } else if (as || bs) {
    // 시작시간 없는 건 뒤로
    return as ? -1 : 1
  }

  // 4) 동률이면 종료시간 → 제목 → id로 안정적 타이브레이크
  const ae = new Date(a.endAt)
  const be = new Date(b.endAt)
  if (ae && be) {
    if (isBefore(ae, be)) {
      return -1
    }
    if (isBefore(be, ae)) {
      return 1
    }
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

      if (!dailyMap[dateKey]) {
        dailyMap[dateKey] = {}
      }

      dailyMap[dateKey][instanceKey] = {
        ...instance,
      }
    })
  })

  return { dailyMap, rawMap }
}

export function expandRecurringRoutine(routine, monthKey) {
  const recurrence = routine.recurrenceRule
  const { rangeStart, rangeEnd } = getMonthRange(monthKey)

  // 비반복 루틴 (dailyProgress 기반)
  if (!recurrence || !recurrence.rruleStr) {
    return (routine.dailyProgress ?? [])
      .filter((progress) => {
        const date = new Date(progress.taskDate)
        return date >= rangeStart && date <= rangeEnd
      })
      .map((progress) => {
        const dateKey = format(new Date(progress.taskDate), 'yyyy-MM-dd')
        return {
          id: progress.dailyTaskId,
          routineTaskId: routine.id,
          title: routine.title,
          time: routine.routineTimeInfo?.time?.slice(0, 5) ?? '',
          currentValue: progress.currentValue ?? 0,
          targetValue: routine.targetValue ?? 1,
          type: routine.taskType,
          instanceDate: dateKey,
          completed: progress.completed ?? false,
        }
      })
  }

  // 반복 규칙이 있는 루틴
  const startStr = recurrence.dtstart || routine.routineTimeInfo?.startDate
  if (!startStr) {
    return []
  }

  const startDate = parseISO(startStr)
  const endStr = routine.routineTimeInfo?.untilDate || startStr
  const endDate = parseISO(endStr)
  const durationMs = endDate.getTime() - startDate.getTime()

  const event = createIcalComponent(recurrence.rruleStr, startDate)
  const iterator = event.iterator(event.startDate)

  const result = []
  const seenDates = new Set()

  // ✅ dailyProgress를 Map으로 변환 (날짜 → progress 객체 전체)
  const progressMap = new Map(
    (routine.dailyProgress || []).map((p) => [
      format(new Date(p.taskDate), 'yyyy-MM-dd'),
      {
        id: p.dailyTaskId,
        currentValue: p.currentValue ?? 0,
        targetValue: p.targetValue ?? routine.targetValue ?? 1,
        taskDate: p.taskDate,
        completed: p.completed ?? false,
      },
    ]),
  )
  let next

//  rangeStart 이전 반복들을 빠르게 스킵
  while ((next = iterator.next())) {
    const nextDate = next.toJSDate()
    if (nextDate >= rangeStart) {
      break
    }
  }

// 💡 next가 rangeStart 이상인 상태에서 루프 시작
  if (next && next.toJSDate() <= rangeEnd) {
    do {
      const nextDate = next.toJSDate()
      const dateKey = format(nextDate, 'yyyy-MM-dd')
      if (!seenDates.has(dateKey)) {
        seenDates.add(dateKey)

        const start = new Date(nextDate)
        const end = new Date(start.getTime() + durationMs)
        const progress = progressMap.get(dateKey)

        result.push({
          id: progress?.id ?? `${routine.id}@${dateKey}`,
          routineTaskId: routine.id,
          title: routine.title,
          time: routine.routineTimeInfo?.time?.slice(0, 5) ?? '',
          currentValue: progress?.currentValue ?? 0,
          targetValue: progress?.targetValue ?? routine.targetValue ?? 1,
          completed: progress?.completed ?? false,
          date: progress?.taskDate ?? dateKey,
          type: routine.taskType,
          instanceDate: dateKey,
          startAt: start.toISOString(),
          endAt: end.toISOString(),
        })
      }
    } while ((next = iterator.next()) && next.toJSDate() <= rangeEnd)
  }

  return result
}

export function expandRoutinesByDay(routines, monthKey) {
  const dailyMap = {}
  const rawMap = {}

  routines.forEach((task) => {
    rawMap[task.id] = task

    const instances = expandRecurringRoutine(task, monthKey)

    instances.forEach((instance) => {
      const dateKey = instance.instanceDate
      const instanceKey = `${instance.id}@${dateKey}`

      if (!dailyMap[dateKey]) {
        dailyMap[dateKey] = {}
      }

      dailyMap[dateKey][instanceKey] = { ...instance }
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
    if (date > rangeEnd) {
      break
    }
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
