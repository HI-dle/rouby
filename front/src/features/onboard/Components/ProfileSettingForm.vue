<template>
  <div class="text-center mt-12 space-y-6 w-full text-main-color">
    <div>
      <p class="text-base">{{ userName }}님은 {{ selectedHealthFirst }}(이)가 있으시군요!</p>
      <p class="text-base">루비를 시작하기 전,</p>
      <p class="text-base">{{ userName }}님의 직업이나 현재 관심사에 대해 알고싶어요!</p>
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
import { computed, watch } from 'vue'
import { useKeywordForm } from '@/shared/composable/useKeywordForm.js'
import KeywordTag from '@/components/common/KeywordTag.vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore.js'

const store = useOnboardStore()

const props = defineProps({
  userName: {
    type: String,
    required: true,
  },
  selectedHealth: {
    type: [String, Array],  // string 또는 배열 가능하게 수정
    required: true,
  },
  exampleKeywords: {
    type: Array,
    default: () => ['백엔드 개발자', '취준', '시험', '승진'],
  },
})

// props는 항상 string 타입으로 통일
const userName = computed(() => props.userName || '')
const selectedHealthFirst = computed(() => {
  return Array.isArray(props.selectedHealth)
    ? props.selectedHealth[0] || ''
    : props.selectedHealth || ''
})

// 피니아 저장소에서 개인 키워드 초기값 가져오기 (store.personalKeyword)
const initialKeywords = store.personalKeyword ?? []

// useKeywordForm에 초기값 넣고 최대 10개 제한
const {
  keyword,
  keywordError,
  keywords,
  handleSubmit,
  removeKeyword,
} = useKeywordForm(initialKeywords, 10)

// store.personalKeyword 값이 바뀌면 keywords도 동기화
watch(
  () => store.personalKeyword,
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

// keywords 변경 시 store.personalKeyword에 반영
watch(
  keywords,
  (newKeywords) => {
    store.personalKeyword = [...newKeywords]
  },
  { deep: true }
)

const onNextClick = () => {
  if (keywords.value.length === 0) {
    alert('키워드를 최소 1개 이상 입력해주세요.')
    return false
  }

  store.personalKeyword = [...keywords.value]

  console.log('저장된 키워드:', store.personalKeyword)
  return true
}

defineExpose({ onNextClick })
</script>
