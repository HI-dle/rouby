import { nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { format } from 'date-fns'
import { parseYMD } from '@/shared/utils/dateUtils'
import { getFeedbacks } from './assistantService'
import { useDatePickStore } from '@/stores/useDatePickStore'
import { formatDateTime } from '@/shared/utils/dateTimeUtils'
import { LIST_ERR_MESSAGES } from './constants'

export const useFeedbackList = (initDate = null) => {
  const route = useRoute()
  const router = useRouter()
  const datePickStore = useDatePickStore()

  const nomessages = ref(LIST_ERR_MESSAGES.noContent)
  const messages = ref([])
  const selectedDate = ref(
    initDate ? parseYMD(initDate) : new Date(datePickStore.lastSelectedDate),
  )

  const fetchFeedbacksAsMessageForm = async (date) => {
    messages.value = []

    try {
      const res = await getFeedbacks(format(date, 'yyyy-MM-dd'))
      if (res.feedbacks.length < 1) {
        nomessages.value = LIST_ERR_MESSAGES.noContent
        return
      }

      messages.value = res.feedbacks.flatMap((r) => [
        {
          from: 'user',
          time: formatDateTime(r.createdAt, { type: 'time12format' }),
          txt: r.userInput.replace(/\n/g, '<br>'),
          mood: r.userMood,
        },
        {
          from: 'rouby',
          time: formatDateTime(r.updatedAt, { type: 'time12format' }),
          txt: r.feedbackContent.replace(/\n/g, '<br>'),
        },
      ])
    } catch (e) {
      if (e.code === 'INVALID_REQUEST') {
        nomessages.value = LIST_ERR_MESSAGES.invalidRequest
      } else {
        nomessages.value =
          typeof e === 'string'
            ? e
            : e.message || '피드백을 불러오는 중 오류가 발생했습니다.'
      }
    }
  }

  onMounted(() => {
    const routeDate = route.params.date
    if (routeDate) {
      selectedDate.value = parseYMD(routeDate)

      router.replace({ name: 'daily-feedback-list' })
    }
  })

  watch(selectedDate, (newDate) => {
    if (newDate) {
      fetchFeedbacksAsMessageForm(newDate)
    }
  })

  return {
    selectedDate,
    messages,
    nomessages,
    fetchFeedbacksAsMessageForm,
  }
}
