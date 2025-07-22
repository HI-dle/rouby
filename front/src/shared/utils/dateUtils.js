import {
  startOfWeek,
  addDays,
  startOfMonth,
  addMonths,
  endOfMonth,
} from 'date-fns'

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
