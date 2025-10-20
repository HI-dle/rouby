import { reactive, ref, watch, onMounted } from 'vue'
import { format, parse, isValid } from 'date-fns'
import { getBriefing } from '@/features/assistant/assistantService'
import { useRouter } from 'vue-router'
import { useDatePickStore } from '@/stores/useDatePickStore.js'
import { parseYMD } from '@/shared/utils/dateUtils'
import { marked } from 'marked'

export const useBriefingForm = (initDate) => {
  const router = useRouter()
  const datePickStore = useDatePickStore()
  const selectedDate = ref(
    initDate ? parseYMD(initDate) : new Date(datePickStore.lastSelectedDate),
  )

  const loading = ref(false)
  const error = ref(null)
  const errorModal = reactive({ show: false, msg: '' })

  // 브리핑 데이터 form
  const form = reactive({
    htmlContent: '',
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

      const data = await getBriefing(dateStr)
      if (data) {
        form.htmlContent = marked.parse(data.content)
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

  watch(selectedDate, (newDate) => {
    if (newDate) {
      fetchBriefing(newDate)
    }
  })

  onMounted(() => {
    const routeDate = router.currentRoute.value.params.date
    if (routeDate) {
      selectedDate.value = parseYMD(routeDate)

      router.replace({ name: 'daily-briefing' })
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
