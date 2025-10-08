import { computed, isRef, nextTick, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { format, differenceInCalendarDays, subDays, isSameDay } from 'date-fns'
import { useRoutineStore } from '@/stores/useRoutineStore'
import { useDatePickStore } from '@/stores/useDatePickStore'

export const useRoutineForm = (maybeSelectedDate = null) => {
  const datePickStore = useDatePickStore()
  const routineStore = useRoutineStore()

  const selectedDate =
    maybeSelectedDate && isRef(maybeSelectedDate)
      ? maybeSelectedDate
      : ref(maybeSelectedDate ?? null)

  const errorModal = reactive({})

  const routinesForSelectedDate = computed(() =>
    selectedDate.value
      ? routineStore.getRoutinesForDate(selectedDate.value)
      : [],
  )

  const routinesForSelectedMonth = ref(
    routineStore.getRoutinesMonthlyByDate(selectedDate.value),
  )

  const startOfThisMonth = computed(() => datePickStore.monthRange.rangeStart)
  const startOfNextMonth = computed(() => datePickStore.monthRange.rangeEnd)

  const router = useRouter()

  const goToRoutineDetail = (routine) => {
    if (!routine?.id || !routine?.instanceDate) return

    goToRoutineDetailOf(routine.id, routine.instanceDate)
  }

  const goToRoutineDetailOf = (id, instanceDate) => {
    router.push({
      name: 'schedule-modify',
      params: {
        id: id,
        date: instanceDate,
      },
    })
  }

  const formatRoutinePeriod = (startAt, endAt) => {
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

  const fetchRoutinesByPeriod = async (rangeStart, rangeEnd) => {
    try {
      await routineStore.loadMonthlyRoutinesIfNeeded(rangeStart, rangeEnd)
    } catch (err) {
      console.error(err)
      errorModal.show = true
      errorModal.msg = err?.message || '스케줄 조회 실패'
    }
  }

  let token = 0
  watch(
    [startOfThisMonth, startOfNextMonth],
    async ([s, n], [os, on]) => {
      if (!s || !n || isSameDay(s, os)) return

      const t = ++token
      await fetchRoutinesByPeriod(s, n)

      if (t !== token) return // 최신 호출만 반영

      routinesForSelectedMonth.value = routineStore.getRoutinesMonthlyByDate(
        selectedDate.value,
      )
    },
    { immediate: true },
  )

  return {
    selectedDate,
    errorModal,
    routinesForSelectedDate,
    routinesForSelectedMonth,
    goToRoutineDetail,
    goToRoutineDetailOf,
    formatRoutinePeriod,
    fetchRoutinesByPeriod,
  }
}
