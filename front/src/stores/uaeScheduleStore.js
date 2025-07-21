import { reactive } from 'vue'
import { defineStore } from 'pinia'
import { format } from 'date-fns'
import { expandSchedulesByDay } from '@/shared/utils/rruleUtils'

export const useScheduleStore = defineStore(
  'schedule',
  () => {
    /**
     * 구조:
     * {
     *   '2025-07': {
     *     '2025-07-01': {
     *       '14@2025-07-01': instance,
     *       ...
     *     },
     *     ...
     *   },
     *   ...
     * }
     */
    const dailySchedules = reactive({})

    /**
     * 월 단위로 기존 데이터 제거 후 새로 채움
     */
    const setMonthlySchedules = (monthKey, schedules) => {
      if (!Array.isArray(schedules)) return

      const dailyMap = expandSchedulesByDay(schedules, monthKey)
      dailySchedules[monthKey] = {}

      for (const dateKey in dailyMap) {
        dailySchedules[monthKey][dateKey] = dailyMap[dateKey]
      }
    }

    /**
     * 해당 월 전체 일정 리스트 반환 (flat)
     */
    const getMonthlySchedules = (monthKey) => {
      const monthData = dailySchedules[monthKey]
      if (!monthData) return null

      return Object.values(monthData).flatMap((instances) =>
        Object.values(instances),
      )
    }

    /**
     * 특정 날짜의 일정 목록 반환
     */
    const getSchedulesForDate = (date) => {
      const dateKey = format(date, 'yyyy-MM-dd')
      const monthKey = dateKey.slice(0, 7)
      return Object.values(dailySchedules[monthKey]?.[dateKey] || {})
    }

    /**
     * instanceKey (ex. 14@2025-07-01)로 일정 1건 조회
     */
    const getScheduleInstanceByKey = (instanceKey) => {
      const [, dateKey] = instanceKey.split('@')
      const monthKey = dateKey.slice(0, 7)

      if (!dailySchedules) return null
      return dailySchedules?.[monthKey]?.[dateKey]?.[instanceKey] || null
    }

    return {
      dailySchedules,
      setMonthlySchedules,
      getMonthlySchedules,
      getSchedulesForDate,
      getScheduleInstanceByKey,
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
