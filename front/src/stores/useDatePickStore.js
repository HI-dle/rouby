import { defineStore } from 'pinia'
import { format, startOfWeek } from 'date-fns'

export const useDatePickStore = defineStore('date-pick', {
  state: () => ({
    weeklySelected: {}, // key: '2024-07-14', value: '2024-07-17'
    monthlySelected: {}, // key: '2024-07', value: '2024-07-01'
    selectedDate: null,
  }),

  actions: {
    setSelectedDate(baseDate, date, isMonthly = false) {
      const key = isMonthly ? format(date, 'yyyy-MM') : format(startOfWeek(baseDate), 'yyyy-MM-dd')

      const value = format(date, 'yyyy-MM-dd')

      if (isMonthly) {
        this.monthlySelected[key] = value
      } else {
        this.weeklySelected[key] = value
      }
      this.selectedDate = value
    },

    getSelectedDate(baseDate, isMonthly = false) {
      const key = isMonthly
        ? format(baseDate, 'yyyy-MM')
        : format(startOfWeek(baseDate), 'yyyy-MM-dd')

      return isMonthly ? this.monthlySelected[key] : this.weeklySelected[key]
    },

    setFinalSelectedDate(date) {
      this.selectedDate = format(date, 'yyyy-MM-dd')
    },
  },

  getters: {
    finalSelectedDate: (state) => {
      return state.selectedDate ?? format(new Date(), 'yyyy-MM-dd')
    },
  },
})
