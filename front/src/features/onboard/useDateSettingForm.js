import { ref, computed } from 'vue'

function parseTime(timeStr, type) {
  if (!timeStr || typeof timeStr !== 'string') {
    return type === 'end'
      ? { period: '오후', hour: 10 } // 22:00
      : { period: '오전', hour: 8 } // 08:00
  }

  const [hourStr] = timeStr.split(':')
  let hour = parseInt(hourStr, 10)

  if (isNaN(hour) || hour < 0 || hour > 23) {
    return { period: '오전', hour: 8 }
  }

  let period = '오전'

  if (hour === 0) {
    hour = 12
  } else if (hour >= 12) {
    period = '오후'
    if (hour > 12) hour -= 12
  }

  return { period, hour }
}

export function useDateSettingForm(storedVal, type) {
  const parsed = parseTime(storedVal, type)

  const form = ref({
    period: parsed.period,
    hour: parsed.hour,
  })

  const periodOptions = [
    { label: '오전', value: '오전' },
    { label: '오후', value: '오후' },
  ]

  const hourOptions = Array.from({ length: 12 }, (_, i) => {
    const hour = i + 1
    return { label: `${hour}시`, value: hour }
  })

  const selectedTime = computed(() => {
    let hour = form.value.hour
    if (form.value.period === '오후' && hour !== 12) hour += 12
    if (form.value.period === '오전' && hour === 12) hour = 0
    return `${String(hour).padStart(2, '0')}:00`
  })

  return {
    form,
    periodOptions,
    hourOptions,
    selectedTime,
  }
}
