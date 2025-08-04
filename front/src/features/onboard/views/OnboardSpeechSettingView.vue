<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <SpeechSettingForm
        v-model:keyword="keyword"
        :keyword-error="keywordError"
        :keywords="keywords"
        @submit="handleSubmit"
        @remove-keyword="removeKeyword"
      />
      <div class="w-full mt-10 pt-10 text-center">
        <button
          @click="onNextLinkClick"
          class="text-indigo-400 underline hover:text-#6667D07A"
        >
          다음 단계로
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { watch } from 'vue'
import { useRouter } from 'vue-router'
import SpeechSettingForm from '@/features/onboard/components/SpeechSettingForm.vue'
import { useKeywordForm } from '@/shared/composable/useKeywordForm'
import { useUserInfoStore } from '@/stores/useUserInfoStore'

const store = useUserInfoStore()
const router = useRouter()
const { keyword, keywordError, keywords, handleSubmit, removeKeyword } =
  useKeywordForm(store.communicationTone ?? [], 3)

const onNextClick = () => {
  if (keywords.value.length === 0) {
    alert('말투를 최소 1개 이상 입력해주세요!')
    return false
  }
  return true
}

const onNextLinkClick = async () => {
  if (onNextClick()) {
    await router.push('/onboarding/alarm-setting')
  }
}

watch(
  keywords,
  (newKeywords) => {
    store.communicationTone = [...newKeywords]
  },
  { deep: true },
)
</script>
