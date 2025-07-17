import { ref, watch } from 'vue'

export function useDatePickerSelectedDate(props, emit, datePickStore) {
  const selectedDate = ref(
    props.modelValue ??
      (datePickStore.lastSelectedDate
        ? new Date(datePickStore.lastSelectedDate)
        : new Date()),
  )

  watch(
    () => props.modelValue,
    (newVal) => {
      if (newVal) selectedDate.value = newVal
    },
  )

  watch(selectedDate, (val) => {
    emit('update:modelValue', val)
    datePickStore.setCurrentSelectedDate(val)
  })

  return selectedDate
}
