<script setup>
import { nextTick, ref } from 'vue'
import { useGoBack } from '@/shared/composable/useGoBack'
import UserInput from '@/components/common/UserInput.vue'
import BaseModal from '@/components/common/BaseModal.vue'
import Messages from '../component/Messages.vue'
import { MAX_LEN, MOODS } from '../constants'
import { useFeedbackForm } from '../useFeedbackForm'

const {
  selectedMoodKey,
  userInput,
  messages,
  resultModal,
  selectMood,
  sendFeedbackRequest,
} = useFeedbackForm()

const userInputRef = ref(null)
const focusUserInput = () => {
  if (userInputRef.value) {
    if (typeof userInputRef.value.focus === 'function') {
      userInputRef.value.focus()
    }
    userInputRef.value.$el.scrollIntoView({
      behavior: 'smooth',
      block: 'end',
    })
  }
}
const onSubmit = (e) => {
  if (e.shiftKey) return
  if (userInput.value.trim() === '') return

  messages.value.push({
    txt: userInput.value.trim(),
    mood: MOODS[selectedMoodKey.value].val,
    from: 'user',
  })

  nextTick(() => {
    focusUserInput()
  })
  sendFeedbackRequest()
}

const { goBackOrPath } = useGoBack()
const handleModalConfirm = () => {
  goBackOrPath('/')
}
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container items-center justify-around">
      <div class="flex flex-col w-full">
        <div class="text-center">
          <div class="text-main-color text-base">오늘 하루를 알려주세요!</div>
          <div class="text-placeholder-color text-sm">
            어떤 하루를 보냈고 기분은 어떠신가요?
          </div>

          <div class="flex justify-center w-full">
            <div
              class="flex flex-wrap gap-x-2 gap-y-2 mt-3 mb-6 items-center justify-center w-3/4"
            >
              <div
                v-for="mood in MOODS"
                :key="mood.key"
                @click="selectMood(mood.key)"
                class="inline-flex items-center bg-white text-text-color text-sm font-bold px-4 py-2 rounded-full border border-border-color"
                :class="[
                  {
                    '!bg-main-color !text-white border-0':
                      mood.key === selectedMoodKey,
                    'hover:bg-gray-100': mood.key !== selectedMoodKey,
                  },
                ]"
              >
                <span>{{ mood.val }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="max-w-xl w-full h-screen">
        <Messages :messages="messages" />
        <div class="w-full">
          <UserInput
            class="scroll-mb-20"
            v-model="userInput"
            ref="userInputRef"
            :placeholder="MOODS[selectedMoodKey].placeholder"
            :maxlength="MAX_LEN.userInput"
            @submit="onSubmit"
            input-class="h-20"
          />
          <p class="text-xs text-gray-500 -mt-2 mx-2">
            입력은 최대 {{ MAX_LEN.userInput }} 자까지 가능합니다. ({{
              userInput.length
            }}
            / {{ MAX_LEN.userInput }} 자)
          </p>
        </div>
      </div>
      <BaseModal
        v-model="resultModal.show"
        :message="resultModal.msg"
        buttonText="확인"
        @close="handleModalConfirm"
      />
    </div>
  </div>
</template>
