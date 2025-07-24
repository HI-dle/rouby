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
import { watch } from 'vue'
import { useKeywordForm } from '@/shared/composable/useKeywordForm.js'
import KeywordTag from '@/components/common/KeywordTag.vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

const store = useOnboardStore()

const props = defineProps({
  userName: String,
  exampleKeywords: {
    type: Array,
    default: () => ['아토피', 'ADHD', '건망증', '당뇨', '건강'],
  },
})

// 피니아 저장소에 저장된 초기 키워드를 초기값으로 넘기고, 최대 10개 제한
const {
  keyword,
  keywordError,
  keywords,
  handleSubmit,
  removeKeyword,
} = useKeywordForm(store.selectedHealth ?? [], 10)

// store.selectedHealth가 변경되면 keywords도 동기화 (초기값 이후 변경 반영용)
watch(
  () => store.selectedHealth,
  (newVal) => {
    if (
      newVal &&
      JSON.stringify(newVal) !== JSON.stringify(keywords.value)
    ) {
      keywords.value = [...newVal]
    }
  },
  { immediate: true }
)

// keywords 변경 시 피니아 저장소에 반영
watch(
  keywords,
  (newKeywords) => {
    store.selectedHealth = [...newKeywords]
  },
  { deep: true }
)

// 다음 단계 버튼 등에서 호출하는 함수
const onNextClick = () => {
  if (keywords.value.length === 0) {
    alert('건강 상태를 최소 1개 이상 입력해주세요!')
    return false
  }

  store.selectedHealth = [...keywords.value]
  console.log('키워드 저장 후 true 반환')
  return true
}

defineExpose({ onNextClick })
</script>
