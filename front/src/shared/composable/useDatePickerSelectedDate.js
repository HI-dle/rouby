import { ref, watch } from 'vue'

export function useDatePickerSelectedDate(props, emit, datePickStore) {
  const selectedDate = ref(props.modelValue ?? new Date(datePickStore.finalSelectedDate))

  watch(
    () => props.modelValue,
    (newVal) => {
      if (newVal) selectedDate.value = newVal
    },
  )

  watch(selectedDate, (val) => {
    emit('update:modelValue', val)
    datePickStore.setFinalSelectedDate(val)
  })

  return selectedDate
}
