import { reactive } from 'vue'
import { defineStore } from 'pinia'
import { format } from 'date-fns'
import {
  buildRRuleString,
  expandSchedulesByDay,
} from '@/shared/utils/rruleUtils'

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

    // 서버 응답 원본 저장: 재계산이 필요할 때 활용
    const rawSchedules = reactive({}) // { '2025-07': [schedule, ...] }

    /**
     * 월 단위로 기존 데이터 제거 후 새로 채움
     */
    const setMonthlySchedules = (monthKey, schedules) => {
      if (!Array.isArray(schedules)) return

      try {
        const { dailyMap, rawMap } = expandSchedulesByDay(schedules, monthKey)
        rawSchedules[monthKey] = rawMap
        dailySchedules[monthKey] = dailyMap
      } catch (error) {
        throw error
      }
    }

    const addRawSchedule = (schedule) => {
      if (!schedule || !schedule.startAt || !schedule.id) return

      const date = new Date(schedule.startAt)
      const monthKey = format(date, 'yyyy-MM')
      schedule.recurrenceRule.rruleStr = buildRRuleString(
        schedule.recurrenceRule,
      )
      if (!rawSchedules[monthKey]) {
        rawSchedules[monthKey] = {}
      }
      rawSchedules[monthKey][schedule.id] = schedule

      recalculateMonth(monthKey)
    }

    /**
     * 월간 키 존재 여부 확인 (중복 조회 방지 등)
     */
    const hasMonth = (monthKey) => !!dailySchedules[monthKey]

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
      if (!instanceKey || !instanceKey.includes('@')) return null
      const [, dateKey] = instanceKey.split('@')
      const monthKey = dateKey.slice(0, 7)

      return dailySchedules?.[monthKey]?.[dateKey]?.[instanceKey] || null
    }

    /**
     * (선택) 원본 스케줄 기준으로 다시 확장 계산
     */
    const recalculateMonth = (monthKey) => {
      const base = rawSchedules[monthKey]
      if (base) {
        setMonthlySchedules(monthKey, Object.values(base))
      }
    }

    return {
      dailySchedules,
      rawSchedules,
      setMonthlySchedules,
      addRawSchedule,
      hasMonth,
      getMonthlySchedules,
      getSchedulesForDate,
      getScheduleInstanceByKey,
      recalculateMonth,
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
