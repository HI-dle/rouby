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
import { getWeekDates } from '@/shared/utils/dateUtils'

export function useDatePickerDates(
  baseDate,
  selectedDate,
  today,
  weekStartsOn = 0,
) {
  if (!baseDate?.value || !selectedDate?.value || !today?.value) {
    console.warn(
      'useDatePickerDates: All date parameters should be reactive references with valid date values',
    )
  }

  const weekDates = computed(() => getWeekDates(baseDate.value, weekStartsOn))

  const calendarDays = computed(() => {
    const start = startOfWeek(startOfMonth(baseDate.value), { weekStartsOn })
    let end = endOfWeek(endOfMonth(baseDate.value), { weekStartsOn })
    const days = eachDayOfInterval({ start, end })

    if (days.length < 42) end = addDays(end, 42 - days.length)
    return eachDayOfInterval({ start, end })
  })

  const currentMonthLabel = computed(() =>
    format(selectedDate.value, 'MMMM yyyy', { locale: ko }),
  )

  const isToday = (date) => isSameDay(date, today.value)
  const isSelected = (date) => isSameDay(date, selectedDate.value)

  return { weekDates, calendarDays, currentMonthLabel, isToday, isSelected }
}
