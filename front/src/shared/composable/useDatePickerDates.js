import { computed } from 'vue'
import {
  startOfWeek,
  addDays,
  startOfMonth,
  endOfMonth,
  endOfWeek,
  eachDayOfInterval,
  format,
  isSameDay,
  isSameMonth,
} from 'date-fns'
import { ko } from 'date-fns/locale'

export function useDatePickerDates(baseDate, selectedDate, today, weekStartsOn = 0) {
  const weekDates = computed(() => {
    const start = startOfWeek(baseDate.value, { weekStartsOn })
    return Array.from({ length: 7 }, (_, i) => addDays(start, i))
  })

  const calendarDays = computed(() => {
    const start = startOfWeek(startOfMonth(baseDate.value), { weekStartsOn })
    let end = endOfWeek(endOfMonth(baseDate.value), { weekStartsOn })
    const days = eachDayOfInterval({ start, end })

    if (days.length < 42) end = addDays(end, 42 - days.length)
    return eachDayOfInterval({ start, end })
  })

  const currentMonthLabel = computed(() => format(selectedDatevalue, 'MMMM yyyy', { locale: ko }))

  const isToday = (date) => isSameDay(date, today.value)
  const isSelected = (date) => isSameDay(date, selectedDate.value)

  return { weekDates, calendarDays, currentMonthLabel, isToday, isSelected }
}
