import { ref, reactive, computed, watch, isRef } from 'vue'
import { useRouter } from 'vue-router'
import { debounce } from 'lodash-es'
import { format, differenceInCalendarDays, subDays } from 'date-fns'

import { useScheduleStore } from '@/stores/useScheduleStore'
import { getMonthRange } from '@/shared/utils/dateUtils'
import { getSchedules } from './scheduleService'

export const useScheduleList = (maybeSelectedDate = null) => {
  const selectedDate =
    maybeSelectedDate && isRef(maybeSelectedDate)
      ? maybeSelectedDate
      : ref(maybeSelectedDate ?? null)

  const errorModal = reactive({})

  const router = useRouter()
  const scheduleStore = useScheduleStore()

  const weekRange = computed(() => {
    if (!selectedDate.value) return { rangeStart: null, rangeEnd: null }
    return getMonthRange(selectedDate.value)
  })

  const startOfThisMonth = computed(() => weekRange.value.rangeStart)
  const startOfNextMonth = computed(() => weekRange.value.rangeEnd)

  const schedulesForSelectedDate = computed(() =>
    selectedDate.value
      ? scheduleStore.getSchedulesForDate(selectedDate.value)
      : [],
  )

  const schedulesForSelectedMonth = ref(
    scheduleStore.getSchedulesMonthlyByDate(selectedDate.value),
  )

  let debounceTimer = null
  const fetchSchedulesByPeriod = async (fromAt, toAt) => {
    if (!scheduleStore.getSchedulesMonthlyByDate(fromAt)) {
      if (debounceTimer) clearTimeout(debounceTimer)

      return new Promise((resolve, reject) => {
        debounceTimer = setTimeout(async () => {
          try {
            const { data } = await getSchedules(fromAt, toAt)
            const key = format(fromAt, 'yyyy-MM')
            scheduleStore.setMonthlySchedules(key, data?.schedules)
            resolve()
          } catch (err) {
            console.error(err)
            errorModal.show = true
            errorModal.msg = err.message || '스케줄 조회 실패'
            reject(err)
          }
        }, 300)
      })
    }
  }

  const goToScheduleDetail = (schedule) => {
    if (!schedule?.id || !schedule?.instanceDate) return

    goToScheduleDetailOf(schedule.id, schedule.instanceDate)
  }

  const goToScheduleDetailOf = (id, instanceDate) => {
    router.push({
      name: 'schedule-modify',
      params: {
        id: id,
        date: instanceDate,
      },
    })
  }

  const formatSchedulePeriod = (startAt, endAt) => {
    const start = new Date(startAt)
    const end = new Date(endAt)
    const endMinus = subDays(end, 1)

    const isAllDayStart = start.getHours() === 0 && start.getMinutes() === 0
    const isAllDayEnd = end.getHours() === 0 && end.getMinutes() === 0
    const isAllDay = isAllDayStart && isAllDayEnd
    const oneDay = differenceInCalendarDays(end, start) === 1

    if (isAllDay && oneDay) return '하루 종일'

    const dateFormat = 'yyyy년 M월 d일'
    const timeFormat = 'HH:mm'
    const formatDateTime = (date, includeTime) =>
      format(date, includeTime ? `${dateFormat} ${timeFormat}` : dateFormat)

    const formattedStart = formatDateTime(start, !isAllDay)
    const formattedEnd = formatDateTime(isAllDay ? endMinus : end, !isAllDay)

    return `${formattedStart} ~ ${formattedEnd}`
  }

  watch([startOfThisMonth, startOfNextMonth], async ([s, n]) => {
    if (s && n) {
      await fetchSchedulesByPeriod(s, n)
      schedulesForSelectedMonth.value = scheduleStore.getSchedulesMonthlyByDate(
        selectedDate.value,
      )
    }
  })

  return {
    selectedDate,
    errorModal,
    schedulesForSelectedDate,
    schedulesForSelectedMonth,
    goToScheduleDetail,
    goToScheduleDetailOf,
    formatSchedulePeriod,
  }
}
