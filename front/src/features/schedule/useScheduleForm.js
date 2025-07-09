import { ref, onMounted, reactive, watch, nextTick } from 'vue'
import { createSchedule } from '@/features/schedule/scheduleService'
import { formatDateTime, toDateTime } from '@/shared/utils/formatDate'
import { validateForm } from './validations'

export const useScheduleForm = () => {
  const errors = reactive({})
  const form = reactive({
    title: '',
    memo: '',
    allDay: false,
    start: formatDateTime(new Date(), { noMins: true }),
    end: formatDateTime(new Date(Date.now() + 3600000), { noMins: true }),
    alarmOffsetMinutes: 'NONE',
    repeat: 'NONE',
    routineStart: formatDateTime(new Date(), { type: 'date' }),
  })

  const onDateTimeInput = (e, key) => {
    const val = e.target.value
    form[key] = form.allDay ? toDateTime(val, 0) : val
  }

  const onSubmit = async () => {
    if (!validateForm(form, errors)) return
    try {
      await createSchedule(form)
    } catch (e) {
      console.error(e)
    }
  }

  ;['title', 'start', 'end', 'routineStart'].forEach((key) => {
    watch(
      () => form[key],
      (newVal) => {
        if (newVal) {
          let errorKey = key
          if (['start', 'end'].includes(key)) errorKey = 'period'
          errors[errorKey] = ''
        }
      },
    )
  })

  return {
    form,
    errors,
    onDateTimeInput,
    onSubmit,
  }
}
