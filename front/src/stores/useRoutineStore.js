import { ref } from 'vue'
import { defineStore } from 'pinia'
import { format } from 'date-fns'
import { getPiniaStorage } from '@/shared/utils/piniaPersistUtils'
import { getRoutines } from '@/features/routine/routineService'
import { expandRoutinesByDay } from '@/shared/utils/rruleUtils.js' // 방금 만든 API 래퍼

export const useRoutineStore = defineStore(
  'routine',
  () => {
    /**
     * 구조:
     * {
     *   '2025-09': {
     *     '2025-09-21': [routine, routine, ...],
     *     '2025-09-22': [routine, ...],
     *     ...
     *   },
     *   ...
     * }
     */
    const dailyRoutines = ref({})

    // 서버 응답 원본 저장: 필요 시 재계산 가능
    const rawRoutines = ref({}) // { '2025-09': [routineTaskResponse, ...] }

    /**
     * 월 단위로 기존 데이터 제거 후 새로 채움
     */
    const setMonthlyRoutines = (monthKey, routines) => {
      if (!Array.isArray(routines)) return

      const { dailyMap, rawMap } = expandRoutinesByDay(routines, monthKey)

      rawRoutines.value[monthKey] = rawMap
      dailyRoutines.value[monthKey] = dailyMap
    }

    /**
     * 월간 키 존재 여부 확인
     */
    const hasMonth = (monthKey) => !!dailyRoutines.value[monthKey]

    /**
     * 해당 월 전체 일정 리스트 반환 (flat)
     */
    const getRoutinesMonthlyByDate = (date) => {
      const monthKey = format(date, 'yyyy-MM')
      const monthData = dailyRoutines.value[monthKey]

      if (!monthData) return null

      return Object.values(monthData).flatMap((instances) =>
        Object.values(instances),
      )
    }

    /**
     * 특정 날짜의 루틴 목록 반환
     */
    const getRoutinesForDate = (date) => {
      const dateKey = format(date, 'yyyy-MM-dd')
      const monthKey = dateKey.slice(0, 7)
      const daily = dailyRoutines.value[monthKey]?.[dateKey]

      return daily ? Object.values(daily) : []
    }

    /**
     * 스토어 초기화
     */
    const reset = () => {
      dailyRoutines.value = {}
      rawRoutines.value = {}
    }

    // ↓↓↓ 공통 로더: 디바운스 + 중복요청 방지 (스케줄 스토어와 동일)
    const _timers = new Map()
    const _inflight = new Map()

    async function loadMonthlyRoutinesIfNeeded(fromDate, toDate, delay = 300) {
      const monthKey = format(fromDate, 'yyyy-MM')

      if (hasMonth(monthKey)) return
      if (_inflight.has(monthKey)) return _inflight.get(monthKey)
      clearTimeout(_timers.get(monthKey))

      const p = new Promise((resolve, reject) => {
        const id = setTimeout(async () => {
          _timers.delete(monthKey)

          try {
            const { data } = await getRoutines(
              format(fromDate, 'yyyy-MM-dd'),
              format(toDate, 'yyyy-MM-dd'),
            )
            setMonthlyRoutines(monthKey, data?.routineTasks || [])
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
      dailyRoutines,
      rawRoutines,
      setMonthlyRoutines,
      hasMonth,
      getRoutinesMonthlyByDate,
      getRoutinesForDate,
      reset,
      loadMonthlyRoutinesIfNeeded,
    }
  },
  {
    persist: { storage: getPiniaStorage() },
  },
)
