import {
  startOfWeek,
  addDays,
  startOfMonth,
  addMonths,
  endOfMonth,
  isSameDay,
} from 'date-fns'
import { formatDateTime } from './dateTimeUtils'

export const getWeekDates = (baseDate, weekStartsOn = 0) => {
  const start = startOfWeek(baseDate, { weekStartsOn })
  return Array.from({ length: 7 }, (_, i) => addDays(start, i))
}

const getWeekdays = (from, end) => {
  const startDate = new Date(from)
  const endDate = new Date(end)
  const includedWeekdays = new Set()

  for (let d = new Date(startDate); d <= endDate; d.setDate(d.getDate() + 1)) {
    includedWeekdays.add(d.getDay())
  }
  return [...includedWeekdays].map((d) => BYDAY[d])
}

export const getWeekRange = (baseDate = new Date(), weekStartsOn = 0) => {
  const startOfThisWeek = startOfWeek(baseDate, { weekStartsOn })
  const startOfNextWeek = addDays(startOfThisWeek, 7)

  return {
    startOfThisWeek,
    startOfNextWeek,
  }
}

export const getMonthRange = (baseDate = new Date()) => {
  const startOfThisMonth = startOfMonth(baseDate)
  const startOfNextMonth = startOfMonth(addMonths(startOfThisMonth, 1))

  return {
    rangeStart: startOfThisMonth,
    rangeEnd: startOfNextMonth,
  }
}

export const getMonthRangeByMonthKey = (monthKey) => {
  const [year, month] = monthKey.split('-').map(Number)
  const base = new Date(year, month - 1, 1)
  return {
    rangeStart: startOfMonth(base),
    rangeEnd: endOfMonth(base),
  }
}

export const getNxtDate = (dateStr) => {
  const date = new Date(dateStr)
  date.setDate(date.getDate() + 1)

  return formatDateTime(date)
}

export const isAllDay = (startAtStr, endAtStr) => {
  const start = new Date(startAtStr)
  const end = new Date(endAtStr)

  const isStartMidnight =
    start.getHours() === 0 &&
    start.getMinutes() === 0 &&
    start.getSeconds() === 0

  const isEndEndOfDay =
    end.getHours() === 23 && end.getMinutes() === 59 && end.getSeconds() === 59

  const isEndNextMidnight =
    end.getHours() === 0 &&
    end.getMinutes() === 0 &&
    end.getSeconds() === 0 &&
    end > start &&
    isSameDay(new Date(start), new Date(end.getTime() - 1))

  return isStartMidnight && (isEndEndOfDay || isEndNextMidnight)
}
