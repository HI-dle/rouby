import { isRef, ref, watch } from 'vue'

export function useDatePickerSelectedDate(propsOrInitial, emit, datePickStore) {
  let selectedDate

  if (propsOrInitial?.modelValue !== undefined) {
    selectedDate = ref(
      propsOrInitial.modelValue ??
        (datePickStore?.lastSelectedDate
          ? new Date(datePickStore.lastSelectedDate)
          : new Date()),
    )

    watch(
      () => propsOrInitial.modelValue,
      (newVal) => {
        if (newVal) selectedDate.value = newVal
      },
    )

    watch(selectedDate, (val) => {
      emit?.('update:modelValue', val)
      datePickStore?.setSelectedDate(val)
    })
  } else {
    selectedDate = ref(
      datePickStore?.lastSelectedDate
        ? new Date(datePickStore.lastSelectedDate)
        : (propsOrInitial ?? new Date()),
    )

    watch(selectedDate, (val) => {
      datePickStore?.setSelectedDate(val)
    })
  }
  return selectedDate
}
