<script setup>
import KeywordTag from '@/components/common/KeywordTag.vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import FieldError from '@/components/common/FieldError.vue'

const props = defineProps({
  userName: String,
  selectedHealth: [String, Array],
  keyword: String,
  keywords: Array,
  keywordError: String,
  handleSubmit: Function,
  removeKeyword: Function,
  exampleKeywords: {
    type: Array,
    default: () => ['백엔드 개발자', '취준', '시험', '승진'],
  },
})

const emit = defineEmits(['update:keyword', 'remove'])

const handleKeywordInput = (val) => {
  emit('update:keyword', val)
}
</script>

<template>
  <div class="text-center space-y-6 w-full text-main-color">
    <div>
      <p class="text-base">
        {{ userName }}님은
        {{
          Array.isArray(selectedHealth) ? selectedHealth[0] : selectedHealth
        }}(이)가 있으시군요!
      </p>
      <p class="text-base">루비를 시작하기 전,</p>
      <p class="text-base">
        {{ userName }}님의 직업이나 현재 관심사에 대해 알고싶어요!
      </p>
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
        :model-value="keyword"
        @update:model-value="handleKeywordInput"
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
