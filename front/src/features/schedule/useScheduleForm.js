import { ref, onMounted, reactive, watch, nextTick } from 'vue'
import { createSchedule } from '@/features/schedule/scheduleService'
import { formatDateTime, toDateTime } from '@/shared/utils/formatDate'
import { validateForm } from './validations'
import { NONE } from './constants'

export const useScheduleForm = () => {
  const errors = reactive({})
  const form = reactive({
    title: '',
    memo: '',
    allDay: false,
    start: formatDateTime(new Date(), { noMins: true }),
    end: formatDateTime(new Date(Date.now() + 3600000), { noMins: true }),
    alarmOffsetMinutes: NONE,
    routineStart: formatDateTime(new Date(), { type: 'date' }),
    repeat: NONE,
  })

  const autoResize = (e) => {
    const el = e.target
    if (!el) return
    el.style.height = 'auto'
    el.style.height = `${el.scrollHeight}px`
  }

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
          delete errors[errorKey]
        }
      },
    )
  })

  return {
    form,
    errors,
    autoResize,
    onDateTimeInput,
    onSubmit,
  }
}
