import { isSameMonth, startOfMonth, startOfWeek } from 'date-fns'

const getMidnightDate = (dateStr) =>
  new Date(new Date(dateStr).setHours(0, 0, 0, 0))

export function useDatePickerState({
  baseDate,
  selectedDate,
  isMonthly,
  datePickStore,
}) {
  const selectDate = (date) => {
    selectedDate.value = date

    if (isMonthly && !isSameMonth(date, baseDate.value)) {
      baseDate.value = startOfMonth(selectedDate.value)
    }
    datePickStore.setSelectedDate(date)
  }

  const syncStoreOnModeSwitch = (isMonthly, wasMonthly) => {
    if (!isMonthly && wasMonthly) {
      // 월간에서 주간으로 이동하는 경우
      baseDate.value = selectedDate.value
    }
    if (isMonthly && !wasMonthly) {
    }
  }

  const handleBaseDateWatch = () => {
    const saved = datePickStore.getSelectedDate(baseDate.value, isMonthly.value)

    if (saved) {
      const parsedDate = getMidnightDate(saved)

      if (!isNaN(parsedDate.getTime())) {
        selectedDate.value = parsedDate
        return
      }
    }

    selectedDate.value = isMonthly.value
      ? getMidnightDate(startOfMonth(baseDate.value))
      : getMidnightDate(startOfWeek(baseDate.value, { weekStartsOn: 0 }))

    datePickStore.setSelectedDate(selectedDate.value)
  }

  return {
    selectDate,
    syncStoreOnModeSwitch,
    handleBaseDateWatch,
  }
}
