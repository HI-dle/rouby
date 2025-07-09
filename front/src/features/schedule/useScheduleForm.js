import { reactive, watch } from 'vue'
import { formatDateTime, toDateTime } from '@/shared/utils/formatDate'

export const useScheduleForm = () => {
  const errors = reactive({})
  const form = reactive({
    title: '',
    memo: '',
    allDay: false,
    start: formatDateTime(new Date(), { noMins: true }),
    end: formatDateTime(new Date(Date.now() + 3600000), { noMins: true }),
    alarmOffsetMinutes: null,
    routineStart: formatDateTime(new Date(), { type: 'date' }),
    repeat: null,
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
  }
}
