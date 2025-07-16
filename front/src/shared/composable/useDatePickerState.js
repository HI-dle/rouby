import { isSameMonth, startOfMonth, startOfWeek } from 'date-fns'

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
    datePickStore.setSelectedDate(baseDate.value, date, isMonthly.value)
  }

  const syncStoreOnWeeklyModeSwitch = (isMonthly, wasMonthly) => {
    if (!isMonthly && wasMonthly) {
      // 월간에서 주간으로 이동하는 경우
      baseDate.value = selectedDate.value
      // 현재 선택 일자를 주간 모드에서도 반영하도록
      datePickStore.setSelectedDate(baseDate.value, selectedDate.value, false)
    }
    if (isMonthly && !wasMonthly) {
      datePickStore.setSelectedDate(
        baseDate.value,
        selectedDate.value,
        isMonthly.value,
      )
    }
  }

  const handleBaseDateWatch = () => {
    const saved = datePickStore.getSelectedDate(baseDate.value, isMonthly.value)

    if (saved) {
      const parsedDate = new Date(saved)
      if (isNaN(parsedDate.getTime())) {
        console.warn('Invalid saved date format:', saved)

        selectedDate.value = isMonthly.value
          ? startOfMonth(baseDate.value)
          : startOfWeek(baseDate.value, { weekStartsOn: 0 })
      } else {
        selectedDate.value = parsedDate
      }
    } else {
      selectedDate.value = isMonthly.value
        ? startOfMonth(baseDate.value)
        : startOfWeek(baseDate.value, { weekStartsOn: 0 })
      datePickStore.setSelectedDate(
        baseDate.value,
        selectedDate.value,
        isMonthly.value,
      )
    }
  }

  return {
    selectDate,
    syncStoreOnWeeklyModeSwitch,
    handleBaseDateWatch,
  }
}
