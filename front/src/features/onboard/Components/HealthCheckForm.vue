<template>
  <div class="text-center mt-12 space-y-6 w-full text-main-color">
    <div>
      <p class="text-base">루비를 시작하기 전,</p>
      <p class="text-base">{{ userName }}님의 건강 상태에 대해 알고싶어요!</p>
      <p class="text-sm text-#6667D07A mt-2">여러 키워드를 추가할 수 있어요!</p>
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
        placeholder="키워드를 추가해주세요."
        @submit="handleSubmit"
        :error="keywordError"
      />
      <FieldError :message="keywordError" />
      <p class="float-left text-s text-#6667D07A mt-2 ml-2.5">
        ex) {{ exampleKeywords.join(', ') }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { useKeywordForm } from '@/features/onboard/useKeywordForm.js'
import KeywordTag from '@/components/common/KeywordTag.vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import { watch } from 'vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

const store = useOnboardStore()

const props = defineProps({
  userName: String,
  exampleKeywords: {
    type: Array,
    default: () => ['아토피', 'ADHD', '건망증', '당뇨', '건강'],
  },
})

const {
  keyword,
  keywordError,
  keywords,
  handleSubmit,
  removeKeyword,
} = useKeywordForm()

const onNextClick = () => {
  if (keywords.value.length === 0) {
    alert('키워드를 최소 1개 이상 입력해주세요.')
    return false
  }

  store.keywords = [...keywords.value]
  store.selectedHealth = store.keywords[0]

  console.log('저장된 키워드:', store.keywords)
  console.log('선택된 건강 상태:', store.selectedHealth)

  return true
}

defineExpose({ onNextClick })
</script>

