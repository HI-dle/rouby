import { defineStore } from 'pinia'
import { format, startOfWeek } from 'date-fns'
import { computed, ref } from 'vue'

export const useDatePickStore = defineStore(
  'date-pick',
  () => {
    // state
    const weeklySelected = ref({}) // key: '2024-07-14', value: '2024-07-17'
    const monthlySelected = ref({}) // key: '2024-07', value: '2024-07-01'
    const selectedDate = ref(null)

    // actions
    function setSelectedDate(baseDate, date, isMonthly = false) {
      const key = isMonthly
        ? format(date, 'yyyy-MM')
        : format(startOfWeek(baseDate), 'yyyy-MM-dd')

      const value = format(date, 'yyyy-MM-dd')

      if (isMonthly) {
        monthlySelected.value[key] = value
      } else {
        weeklySelected.value[key] = value
      }

      selectedDate.value = value
    }

    function getSelectedDate(baseDate, isMonthly = false) {
      const key = isMonthly
        ? format(baseDate, 'yyyy-MM')
        : format(startOfWeek(baseDate), 'yyyy-MM-dd')

      return isMonthly ? monthlySelected.value[key] : weeklySelected.value[key]
    }

    function setCurrentSelectedDate(date) {
      selectedDate.value = format(date, 'yyyy-MM-dd')
    }

    // getters
    const lastSelectedDate = computed(() => {
      return selectedDate.value ?? format(new Date(), 'yyyy-MM-dd')
    })

    return {
      // state
      weeklySelected,
      monthlySelected,
      selectedDate,

      // actions
      setSelectedDate,
      getSelectedDate,
      setCurrentSelectedDate,

      // getters
      lastSelectedDate,
    }
  },
  {
    persist: {
      storage: JSON.parse(localStorage.getItem('alwaysLogin') || 'false')
        ? localStorage
        : sessionStorage,
    },
  },
)
