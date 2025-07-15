import { startOfMonth, startOfWeek } from 'date-fns'

export function useDatePickerState({ baseDate, selectedDate, isMonthly, datePickStore }) {
  const selectDate = (date) => {
    selectedDate.value = date
    datePickStore.setSelectedDate(baseDate.value, date, isMonthly.value)
  }

  const syncStoreOnModeSwitch = (newVal, oldVal) => {
    if (!newVal && oldVal) {
      baseDate.value = selectedDate.value
      datePickStore.setSelectedDate(baseDate.value, selectedDate.value, false)
    }
  }

  const handleBaseDateWatch = () => {
    const saved = datePickStore.getSelectedDate(baseDate.value, isMonthly.value)

    if (saved) {
      selectedDate.value = new Date(saved)
    } else {
      selectedDate.value = isMonthly.value
        ? startOfMonth(baseDate.value)
        : startOfWeek(baseDate.value, { weekStartsOn: 0 })
      datePickStore.setSelectedDate(baseDate.value, selectedDate.value, isMonthly.value)
    }
  }

  return {
    selectDate,
    syncStoreOnModeSwitch,
    handleBaseDateWatch,
  }
}
