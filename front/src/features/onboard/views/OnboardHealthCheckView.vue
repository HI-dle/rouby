<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <HealthCheckForm
        :user-name="store.nickname"
        v-model:keyword="keyword"
        :keywords="keywords"
        :keyword-error="keywordError"
        :handle-submit="handleSubmit"
        :remove-keyword="removeKeyword"
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
import { useRouter } from 'vue-router'
import HealthCheckForm from '@/features/onboard/components/HealthCheckForm.vue'
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { useKeywordForm } from '@/shared/composable/useKeywordForm'
import { watch } from 'vue'

const store = useUserInfoStore()
const router = useRouter()

// 피니아 저장소에 저장된 초기 키워드를 초기값으로 넘기고, 최대 10개 제한
const { keyword, keywordError, keywords, handleSubmit, removeKeyword } =
  useKeywordForm(store.healthStatusKeywords ?? [], 10)

const onNextClick = () => {
  if (keywords.value.length === 0) {
    alert('건강 상태를 최소 1개 이상 입력해주세요!')
    return false
  }
  return true
}

const onNextLinkClick = async () => {
  if (onNextClick()) {
    await router.push('/onboarding/profile-setting')
  }
}

watch(
  keywords,
  (newKeywords) => {
    store.healthStatusKeywords = [...newKeywords]
  },
  { deep: true },
)
</script>
