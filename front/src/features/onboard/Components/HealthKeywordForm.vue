<!-- components/onboarding/HealthKeywordForm.vue -->
<template>
  <div class="text-center mt-12 space-y-6 w-full text-indigo-600">
    <div>
      <p class="text-base">루비를 시작하기 전,</p>
      <p class="text-base">{{ userName }}님의 건강 상태에 대해 알고싶어요!</p>
      <p class="text-sm text-indigo-300 mt-2">여러 키워드를 추가할 수 있어요!</p>
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
      <p class="float-left text-s text-indigo-300 mt-2 ml-2.5">
        ex) {{ exampleKeywords.join(', ') }}
      </p>
    </div>
  </div>
</template>

<script setup>
import KeywordTag from '@/components/common/KeywordTag.vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import { useHealthKeywordForm } from '@/features/onboard/useHealthKeywordForm.js'
import FieldError from '@/components/common/FieldError.vue'

const props = defineProps({
  userName: {
    type: String,
    required: true,
  },
  exampleKeywords: {
    type: Array,
    required: false,
    default: () => ['아토피', 'ADHD', '건망증', '당뇨', '건강'],
  },
})

const {
  keyword,
  keywordError,
  keywords,
  handleSubmit,
  removeKeyword,
} = useHealthKeywordForm()
</script>
