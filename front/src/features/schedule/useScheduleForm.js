import { nextTick, reactive, ref, watch } from 'vue'
import { addDays, subDays } from 'date-fns'
import {
  convertDateToDateTime,
  formatDateTime,
  isMidnight,
} from '@/shared/utils/dateTimeUtils'
import { validateForm } from './validations'
import { createSchedule } from './scheduleService'
import { useScheduleStore } from '@/stores/useScheduleStore'
import { useDatePickStore } from '@/stores/useDatePickStore'

export const useScheduleForm = (initValues = {}) => {
  const datePickStore = useDatePickStore()
  const scheduleStore = useScheduleStore()

  const createInitialForm = () => {
    let baseDate = new Date()

    if (initValues.start) {
      baseDate = new Date(initValues.start)
      datePickStore.setSelectedDate(baseDate)
    } else if (datePickStore.selectedDate) {
      const [year, month, day] = datePickStore.selectedDate
        .split('-')
        .map(Number)
      baseDate = new Date(
        year,
        month - 1,
        day,
        baseDate.getHours(),
        baseDate.getMinutes(),
      )
    }

    const endDate =
      initValues.end != null
        ? new Date(
            initValues.allDay ? subDays(initValues.end, 1) : initValues.end,
          )
        : new Date(baseDate.getTime() + 60 * 60 * 1000)

    return reactive({
      title: '',
      memo: '',
      allDay: initValues.allDay ?? false,
      start: formatDateTime(baseDate, { noMins: true }),
      end: formatDateTime(endDate, { noMins: true }),
      alarmOffsetMinutes: null,
      routineStart: formatDateTime(baseDate, { type: 'date' }),
      repeat: null,
    })
  }
  const form = createInitialForm()
  const isSubmitting = ref(false)
  const errors = reactive({})
  const errorModal = reactive({})
  const inputRefs = {}

  const onDateTimeInput = (e, key) => {
    const val = e.target.value
    form[key] = form.allDay ? convertDateToDateTime(val, 0) : val
  }

  const focusFirstInvalidInput = async () => {
    for (const key in errors) {
      if (errors[key]) {
        await nextTick()
        inputRefs[key]?.focus()
        break
      }
    }
  }

  const refetchSchedulesByPeriod = async () => {
    scheduleStore.reset()

    const monthRange = datePickStore.monthRange
    await scheduleStore.loadMonthlySchedulesIfNeeded(
      monthRange.rangeStart,
      monthRange.rangeEnd,
    )
  }

  const onSubmit = async (onSuccess, onError) => {
    if (isSubmitting.value) return

    if (!validateForm(form, errors)) {
      focusFirstInvalidInput()
      return false
    }

    isSubmitting.value = true
    try {
      const schedule = await createSchedule(form)
      await refetchSchedulesByPeriod()
      await nextTick()
      onSuccess?.(schedule.id)
      return schedule.id
    } catch (err) {
      const msg = err.response?.data?.message || err.message || '저장 실패'
      onError?.(msg)
      return null
    } finally {
      isSubmitting.value = false
    }
  }

  // 에러 클리어링 watchers
  ;['title', 'start', 'end', 'routineStart'].forEach((key) => {
    watch(
      () => form[key],
      (newVal) => {
        if (newVal) {
          let errorKey = key
          if (['start', 'end'].includes(key)) errorKey = 'period'
          delete errors[errorKey]
        }
      },
    )
  })

  watch(
    () => form.allDay,
    (newVal) => {
      const endDate = new Date(form.end)

      if (!newVal && isMidnight(endDate)) {
        form.end = formatDateTime(addDays(form.end, 1))
      }
      if (newVal && isMidnight(endDate)) {
        form.end = formatDateTime(subDays(form.end, 1))
      }
    },
    { immediate: false },
  )

  return {
    form,
    isSubmitting,
    errors,
    inputRefs,
    errorModal,
    onDateTimeInput,
    onSubmit,
  }
}
