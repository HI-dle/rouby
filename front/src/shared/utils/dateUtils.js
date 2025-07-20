import { startOfWeek, addDays, startOfMonth, addMonths } from 'date-fns'

export const getWeekDates = (baseDate, weekStartsOn = 0) => {
  const start = startOfWeek(baseDate, { weekStartsOn })
  return Array.from({ length: 7 }, (_, i) => addDays(start, i))
}

export const getWeekRange = (baseDate = new Date(), weekStartsOn = 0) => {
  const startOfThisWeek = startOfWeek(baseDate, { weekStartsOn })
  const startOfNextWeek = new Date(startOfThisWeek)
  startOfNextWeek.setDate(startOfThisWeek.getDate() + 7)

  return {
    startOfThisWeek,
    startOfNextWeek,
  }
}

export const getMonthRange = (baseDate = new Date()) => {
  const startOfThisMonth = startOfMonth(baseDate)
  const startOfNextMonth = startOfMonth(addMonths(startOfThisMonth, 1))

  return {
    startOfThisMonth,
    startOfNextMonth,
  }
}
