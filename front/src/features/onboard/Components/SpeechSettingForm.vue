<template>
  <div class="text-center mt-12 space-y-6 w-full text-main-color">
    <div>
      <p class="text-base">제가 어떤 말투이길 바라시나요?</p>
    </div>

    <!-- 키워드 태그 목록 -->
    <div class="flex flex-wrap justify-center gap-2 mt-4">
      <KeywordTag
        v-for="k in keywords"
        :key="k"
        :label="k"
        @remove="removeKeyword(k)"
      />
    </div>

    <!-- 입력창 -->
    <div class="mt-10">
      <UserSettingInput
        v-model="keyword"
        placeholder="귀여운, 건방진, 까칠한, 겸손한"
        @submit="handleSubmit"
        :error="keywordError"
      />
      <FieldError :message="keywordError" />
    </div>
  </div>
</template>

<script setup>
import { useKeywordForm } from '@/shared/composable/useKeywordForm.js'
import KeywordTag from '@/components/common/KeywordTag.vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

const store = useOnboardStore()

const {
  keyword,
  keywordError,
  keywords,
  handleSubmit,
  removeKeyword,
} = useKeywordForm(3)

const onNextClick = () => {
  if (keywords.value.length === 0) {
    alert('말투를 최소 1개 이상 입력해주세요!')
    return false
  }

  store.speechType = [...keywords.value]

  console.log('키워드 저장 후 true 반환')
  return true
}

defineExpose({ onNextClick })
</script>

