import { nextTick, reactive, watch } from 'vue'
import { formatDateTime, toDateTime } from '@/shared/utils/formatDate'
import { validateForm } from './validations'
import { createSchedule } from './scheduleService'

export const useScheduleForm = () => {
  const errors = reactive({})
  const errorModal = reactive({})
  const inputRefs = {}

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

  const onDateTimeInput = (e, key) => {
    const val = e.target.value
    form[key] = form.allDay ? toDateTime(val, 0) : val
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
    if (!validateForm(form, errors)) {
      focusFirstInvalidInput()
      return false
    }

    try {
      const scheduleId = await createSchedule(form)
      onSuccess?.(scheduleId)
      return scheduleId
    } catch (err) {
      const msg = err.response?.data?.message || err.message || '저장 실패'
      onError?.(msg)
      return null
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
    errors,
    inputRefs,
    errorModal,
    onDateTimeInput,
    onSubmit,
  }
}
