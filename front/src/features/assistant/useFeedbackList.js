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
  const parsedDate = initDate ? parseYMD(initDate) : null
  const selectedDate = parsedDate
    ? ref(parsedDate)
    : ref(new Date(datePickStore.lastSelectedDate))

  const scrollToLastUserDiv = () => {
    const userDivs = document.querySelectorAll('div.from-user')

    if (userDivs.length > 0) {
      const lastUserDiv = userDivs[userDivs.length - 1]
      lastUserDiv.scrollIntoView({ behavior: 'smooth', block: 'start' })
    }
  }

  const fetchFeedbacksAsMessageForm = async (date) => {
    messages.value = []

    try {
      const res = await getFeedbacks(format(date, 'yyyy-MM-dd'))
      if (res.feedbacks.length < 1) {
        nomessages.value = LIST_ERR_MESSAGES.noContent
        return
      }

      for (let r of res.feedbacks) {
        messages.value.push({
          from: 'user',
          time: formatDateTime(r.createdAt, { type: 'time12format' }),
          txt: r.userInput.replace(/\n/g, '<br>'),
          mood: r.userMood,
        })
        messages.value.push({
          from: 'rouby',
          time: formatDateTime(r.updatedAt, { type: 'time12format' }),
          txt: r.feedbackContent.replace(/\n/g, '<br>'),
        })
      }
    } catch (e) {
      if (e.code === 'INVALID_REQUEST') {
        nomessages.value = LIST_ERR_MESSAGES.invalidRequest
      }
    }

    await nextTick()
    scrollToLastUserDiv()
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
  return { selectedDate, messages, nomessages, fetchFeedbacksAsMessageForm }
}
