import { ref, computed } from 'vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

export function useDateSettingForm() {
  const store = useOnboardStore()

  const form = ref({
    period: '오전',
    hour: 8,
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

  const onNextClick = () => {
    if (!form.value.hour || !form.value.period) {
      alert('시간을 선택해주세요!')
      return false
    }

    store.startTime = selectedTime.value
    console.log('✅ 저장된 시간:', store.startTime)
    return true
  }

  return {
    form,
    periodOptions,
    hourOptions,
    selectedTime,
  }
}
