import { ref, reactive, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { debounce } from 'lodash-es'
import { format, differenceInCalendarDays, subDays } from 'date-fns'

import { useScheduleStore } from '@/stores/useScheduleStore'
import { getMonthRange } from '@/shared/utils/dateUtils'
import { getSchedules } from './scheduleService'

export const useScheduleList = () => {
  const selectedDate = ref(null)
  const errorModal = reactive({})

  const router = useRouter()
  const scheduleStore = useScheduleStore()

  const weekRange = computed(() => {
    if (!selectedDate.value) return { rangeStart: null, rangeEnd: null }
    return getMonthRange(selectedDate.value)
  })

  const startOfThisMonth = computed(() => weekRange.value.rangeStart)
  const startOfNextMonth = computed(() => weekRange.value.rangeEnd)

  const debouncedFetch = debounce(async (fromAt, toAt) => {
    try {
      const { data } = await getSchedules(fromAt, toAt)
      const key = format(fromAt, 'yyyy-MM')
      scheduleStore.setMonthlySchedules(key, data?.schedules)
    } catch (err) {
      console.error(err)
      const msg =
        err?.response?.data?.message || err?.message || '스케줄 조회 실패'
      errorModal.show = true
      errorModal.msg = msg
    }
  }, 300)

  const fetchSchedulesByPeriod = (fromAt, toAt) => {
    const key = format(fromAt, 'yyyy-MM')
    if (!scheduleStore.getMonthlySchedules(key)) {
      debouncedFetch(fromAt, toAt)
    }
  }

  const schedulesForSelectedDate = computed(() =>
    selectedDate.value
      ? scheduleStore.getSchedulesForDate(selectedDate.value)
      : [],
  )

  const goToScheduleDetail = (schedule) => {
    if (!schedule?.id || !schedule?.instanceDate) return

    router.push({
      name: 'schedule-modify',
      params: {
        id: schedule.id,
        date: schedule.instanceDate,
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

  watch([startOfThisMonth, startOfNextMonth], ([s, n]) => {
    if (s && n) {
      fetchSchedulesByPeriod(s, n)
    }
  })

  return {
    selectedDate,
    errorModal,
    schedulesForSelectedDate,
    goToScheduleDetail,
    formatSchedulePeriod,
  }
}
