import { reactive, ref, watch } from 'vue'
import { requestFeedback } from './assistantService'
import { MAX_LEN, REQ_ERR_MESSAGES } from './constants'

export const useFeedbackForm = () => {
  const selectedMoodKey = ref('soso')
  const selectMood = (moodKey) => {
    selectedMoodKey.value = moodKey
  }

  const resultModal = reactive({})
  const userInput = ref('')
  const messages = ref([])

  const sendFeedbackRequest = async () => {
    try {
      const res = await requestFeedback({
        userInput: userInput.value,
        userMood: selectedMoodKey.value,
      })

      userInput.value = ''
      messages.value.push({
        txt: '지금 피드백을 작성하고 있습니다.',
        ready: true,
        from: 'rouby',
      })

      resultModal.msg =
        '요청하신 피드백을 작성하고 있습니다. <br /> 피드백 작성이 완료되면 알림으로 알려드릴게요!'
      resultModal.show = true
    } catch (err) {
      const msg =
        err?.response?.data?.message ??
        REQ_ERR_MESSAGES[err?.code] ??
        (typeof err === 'string' ? err : err?.message) ??
        '피드백 요청에 실패하였습니다.'
      resultModal.msg = msg
      resultModal.show = true
    }
  }

  watch(userInput, (newValue) => {
    if (newValue.length > MAX_LEN.userInput) {
      userInput.value = newValue.substring(0, MAX_LEN.userInput)
    }
  })
  return {
    selectedMoodKey,
    userInput,
    messages,
    resultModal,
    selectMood,
    sendFeedbackRequest,
  }
}
