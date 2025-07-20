import {nextTick, reactive} from 'vue'
import {validateForm} from './validations'
import {createRoutineTask} from './routineTaskService'

export function useRoutineTaskForm() {
  const form = reactive({
    title: '',
    startDate: '',
    until: '',
    time: '07:00',
    taskType: 'CHECK',
    targetValue: 1,
    alarmOffsetMinutes: null,
    byDays: [],
    memo: '',
  })

  const errors = reactive({})
  const errorModal = reactive({ show: false, msg: '' })
  const inputRefs = reactive({})

  const onDateTimeInput = (key, value) => {
    form.value[key] = value
  }

  const focusFirstInvalidInput = async () => {
    for (const key in errors) {
      if (errors[key]) {
        await nextTick()
        inputRefs[key]?.focus?.()
        break
      }
    }
  }

  const buildPayload = () => {
    return {
      title: form.title,
      startDate: form.startDate,
      time: form.time,
      taskType: form.taskType,
      targetValue: form.targetValue,
      alarmOffsetMinutes: form.alarmOffsetMinutes,
      byDays: form.byDays,
      memo: form.memo,
      until: form.until ? toZonedISOString(form.until) : null, // ✅ 변환
    }
  }

  const toZonedISOString = (localDateTimeStr) => {
    const date = new Date(localDateTimeStr)

    const pad = (n) => String(Math.floor(Math.abs(n))).padStart(2, '0')

    const offsetMin = -date.getTimezoneOffset()
    const sign = offsetMin >= 0 ? '+' : '-'
    const hours = pad(offsetMin / 60)
    const minutes = pad(offsetMin % 60)
    const offset = `${sign}${hours}:${minutes}`

    const localISOString = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
      .toISOString()
      .slice(0, -1) // remove 'Z'

    return `${localISOString}${offset}`
  }

  const onSubmit = async (onSuccess, onError) => {
    if (!validateForm(form, errors)) {
      console.log(form)
      focusFirstInvalidInput()
      return
    }

    try {
      const payload = buildPayload()

      const id = await createRoutineTask(payload)
      onSuccess?.(id)
      return id
    } catch (err) {
      const msg = err.response?.data?.message || err.message || '저장 실패'
      errorModal.msg = msg
      errorModal.show = true
      onError?.(msg)
    }
  }

  return {
    form,
    errors,
    errorModal,
    inputRefs,
    onDateTimeInput,
    onSubmit,
  }
}
