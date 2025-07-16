import { nextTick, reactive, ref, watch } from 'vue'
import { convertDateToDateTime, formatDateTime } from '@/shared/utils/dateTimeUtil'
import { validateForm } from './validations'
import { createSchedule } from './scheduleService'

export const useScheduleForm = () => {
  const createInitialForm = () => {
    const now = new Date()
    const oneHourLater = new Date(now.getTime() + 3600000)

    return reactive({
      title: '',
      memo: '',
      allDay: false,
      start: formatDateTime(now, { noMins: true }),
      end: formatDateTime(oneHourLater, { noMins: true }),
      alarmOffsetMinutes: null,
      routineStart: formatDateTime(now, { type: 'date' }),
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

  const onSubmit = async (onSuccess, onError) => {
    if (isSubmitting.value) return

    if (!validateForm(form, errors)) {
      focusFirstInvalidInput()
      return false
    }

    isSubmitting.value = true
    try {
      const scheduleId = await createSchedule(form)
      onSuccess?.(scheduleId)
      return scheduleId
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
