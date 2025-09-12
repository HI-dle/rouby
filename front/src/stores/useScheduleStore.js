import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import { format } from 'date-fns'
import { expandSchedulesByDay } from '@/shared/utils/rruleUtils'
import { getPiniaStorage } from '@/shared/utils/piniaPersistUtils'
import { getSchedules } from '@/features/schedule/scheduleService'

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
    const dailySchedules = ref({})

    // 서버 응답 원본 저장: 재계산이 필요할 때 활용
    const rawSchedules = ref({}) // { '2025-07': [schedule, ...] }

    /**
     * 월 단위로 기존 데이터 제거 후 새로 채움
     */
    const setMonthlySchedules = (monthKey, schedules) => {
      if (!Array.isArray(schedules)) return

      const { dailyMap, rawMap } = expandSchedulesByDay(schedules, monthKey)

      rawSchedules.value[monthKey] = rawMap
      dailySchedules.value[monthKey] = dailyMap
    }

    const addRawSchedule = (schedule) => {
      if (!schedule || !schedule.startAt || !schedule.id) return

      const date = new Date(schedule.startAt)
      const monthKey = format(date, 'yyyy-MM')

      if (schedule.recurrenceRule) {
        schedule.recurrenceRule.rruleStr = buildRRuleString(
          schedule.recurrenceRule,
        )
      }

      rawSchedules.value[monthKey] = rawMap
      dailySchedules.value[monthKey] = dailyMap
      if (!rawSchedules.value[monthKey]) {
        rawSchedules.value[monthKey] = {}
      }
      rawSchedules.value[monthKey][schedule.id] = schedule

      recalculateMonth(monthKey)
    }

    /**
     * 월간 키 존재 여부 확인 (중복 조회 방지 등)
     */
    const hasMonth = (monthKey) => !!dailySchedules.value[monthKey]

    /**
     * 해당 월 전체 일정 리스트 반환 (flat)
     */
    const getSchedulesMonthlyByDate = (date) => {
      const monthKey = format(date, 'yyyy-MM')
      const monthData = dailySchedules.value[monthKey]

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

      return Object.values(dailySchedules.value[monthKey]?.[dateKey] || {})
    }

    /**
     * instanceKey (ex. 14@2025-07-01)로 일정 1건 조회
     */
    const getScheduleInstanceByKey = (instanceKey) => {
      if (!instanceKey || !instanceKey.includes('@')) return null
      const [, dateKey] = instanceKey.split('@')
      const monthKey = dateKey.slice(0, 7)

      return dailySchedules.value?.[monthKey]?.[dateKey]?.[instanceKey] || null
    }

    /**
     * (선택) 원본 스케줄 기준으로 다시 확장 계산
     */
    const recalculateMonth = (monthKey) => {
      const base = rawSchedules.value[monthKey]
      if (base) {
        setMonthlySchedules(monthKey, Object.values(base))
      }
    }

    /**
     * 스토어 초기화
     */
    const reset = () => {
      dailySchedules.value = {}
      rawSchedules.value = {}
    }

    // ↓↓↓ 공통 로더: 디바운스 + 중복요청 방지 (키별)
    const _timers = new Map() // monthKey -> timeout id
    const _inflight = new Map() // monthKey -> Promise

    async function loadMonthlySchedulesIfNeeded(fromAt, toAt, delay = 300) {
      const monthKey = format(fromAt, 'yyyy-MM')

      if (hasMonth(monthKey)) return // 캐시 히트

      if (_inflight.has(monthKey)) return _inflight.get(monthKey) // 진행중이면 재활용
      clearTimeout(_timers.get(monthKey))

      const p = new Promise((resolve, reject) => {
        const id = setTimeout(async () => {
          _timers.delete(monthKey)

          try {
            const { data } = await getSchedules(fromAt, toAt)
            setMonthlySchedules(monthKey, data?.schedules || [])
            resolve()
          } catch (e) {
            reject(e)
          } finally {
            _inflight.delete(monthKey)
          }
        }, delay)
        _timers.set(monthKey, id)
      })

      _inflight.set(monthKey, p)
      return p
    }

    return {
      dailySchedules,
      rawSchedules,
      setMonthlySchedules,
      hasMonth,
      getSchedulesMonthlyByDate,
      getSchedulesForDate,
      getScheduleInstanceByKey,
      recalculateMonth,
      reset,
      loadMonthlySchedulesIfNeeded,
    }
  },
  {
    persist: { storage: getPiniaStorage() },
  },
)
