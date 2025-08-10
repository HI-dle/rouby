<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <!-- 키워드 입력 폼 -->
      <ProfileSettingForm
        :user-name="store.nickname"
        :selected-health="selectedHealthFirst"
        v-model:keyword="keyword"
        :keywords="keywords"
        :keyword-error="keywordError"
        :handle-submit="handleSubmit"
        :remove-keyword="removeKeyword"
      />

      <!-- 다음 단계 이동 -->
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
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { useKeywordForm } from '@/shared/composable/useKeywordForm'
import { watch } from 'vue'
import ProfileSettingForm from '@/features/onboard/components/ProfileSettingForm.vue'

const store = useUserInfoStore()
const router = useRouter()

// useKeywordForm에 초기값 넣고 최대 10개 제한
const { keyword, keywordError, keywords, handleSubmit, removeKeyword } =
  useKeywordForm(store.profileKeywords ?? [], 10)

const selectedHealthFirst = Array.isArray(store.healthStatusKeywords)
  ? store.healthStatusKeywords[0] || ''
  : store.healthStatusKeywords || ''

const onNextClick = () => {
  if (keywords.value.length === 0) {
    alert('키워드를 최소 1개 이상 입력해주세요.')
    return false
  }
  return true
}

const onNextLinkClick = () => {
  if (onNextClick()) {
    router.push('/onboarding/start-date-setting')
  }
}

watch(
  keywords,
  (newKeywords) => {
    store.profileKeywords = [...newKeywords]
  },
  { deep: true },
)
</script>
