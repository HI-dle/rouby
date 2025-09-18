import { reactive, ref, watch, onMounted } from 'vue'
import { format, parse, isValid} from 'date-fns'
import { getBriefing } from '@/features/assistant/assistantService'
import { useRouter } from 'vue-router'

export const useBriefingForm = (initDate) => {
  const router = useRouter()

  // 초기 선택 날짜
  const parseYMD = (d) =>
    typeof d === 'string' ? parse(d, 'yyyy-MM-dd', new Date()) : new Date(d)
  const selectedDate = ref(initDate ? parseYMD(initDate) : new Date())
  const loading = ref(false)
  const error = ref(null)
  const errorModal = reactive({ show: false, msg: '' })

  // 브리핑 데이터 form
  const form = reactive({
    content: '',
    createdAt: format(selectedDate.value, 'yyyy-MM-dd'),
  })

  // 브리핑 조회
  const fetchBriefing = async (targetDate = selectedDate.value) => {
    if (!targetDate) return
    loading.value = true
    error.value = null
    try {
      const dateObj =
        targetDate instanceof Date ? targetDate : parseYMD(targetDate)
      if (!isValid(dateObj)) {
        throw new Error('유효하지 않은 날짜입니다.')
      }
      const dateStr = format(dateObj, 'yyyy-MM-dd')

      // getBriefing이 이미 { content, createdAt } 반환
      const data = await getBriefing(dateStr)
      if (data) {
        form.content = data.content
        form.createdAt = data.createdAt
      }
    } catch (e) {
      error.value = e?.message || '브리핑 조회 실패'
      errorModal.show = true
      errorModal.msg = error.value
    } finally {
      loading.value = false
    }
  }

  // 날짜 바뀌면 자동으로 다시 조회
  watch(selectedDate, (newDate) => {
    if (newDate) fetchBriefing(newDate)
  })

  // 초기 param 처리 + URL 고정
  onMounted(() => {
    const routeDate = router.currentRoute.value.params.date
    if (routeDate) {
      selectedDate.value = parseYMD(routeDate)

      // param 제거하고 URL 고정
      router.replace({ name: 'briefing-daily' })
    } else {
      fetchBriefing(selectedDate.value)
    }
  })

  return {
    form,
    selectedDate,
    loading,
    error,
    errorModal,
    fetchBriefing,
  }
}
