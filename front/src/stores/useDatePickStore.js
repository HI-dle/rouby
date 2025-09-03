import { defineStore } from 'pinia'
import { format, startOfWeek } from 'date-fns'
import { computed, ref } from 'vue'
import { getPiniaStorage } from '@/shared/utils/piniaPersistUtils'

export const useDatePickStore = defineStore(
  'date-pick',
  () => {
    // state
    const weeklySelected = ref({}) // key: '2024-07-14', value: '2024-07-17'
    const monthlySelected = ref({}) // key: '2024-07', value: '2024-07-01'
    const selectedDate = ref(null)

    function getSelectedDate(baseDate, isMonthly = false) {
      const key = isMonthly
        ? format(baseDate, 'yyyy-MM')
        : format(startOfWeek(baseDate), 'yyyy-MM-dd')

      return isMonthly ? monthlySelected.value[key] : weeklySelected.value[key]
    }

    function setSelectedDate(date) {
      selectedDate.value = format(date, 'yyyy-MM-dd')

      monthlySelected.value[format(date, 'yyyy-MM')] = selectedDate.value
      weeklySelected.value[format(startOfWeek(date), 'yyyy-MM-dd')] =
        selectedDate.value
    }

    // getters
    const lastSelectedDate = computed(() => {
      return selectedDate.value ?? format(new Date(), 'yyyy-MM-dd')
    })

    const reset = () => {
      weeklySelected.value = {}
      monthlySelected.value = []
      selectedDate.value = null
    }

    return {
      // state
      weeklySelected,
      monthlySelected,
      selectedDate,

      // actions
      getSelectedDate,
      setSelectedDate,

      // getters
      lastSelectedDate,

      // reset
      reset,
    }
  },
  {
    persist: {
      storage: getPiniaStorage(),
    },
  },
)
