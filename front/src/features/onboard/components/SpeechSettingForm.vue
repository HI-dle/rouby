<template>
  <div class="text-center space-y-6 w-full text-main-color">
    <div>
      <p class="text-base">제가 어떤 말투이길 바라시나요?</p>
    </div>

    <!-- 키워드 태그 목록 -->
    <div class="flex flex-wrap justify-center gap-2 mt-4">
      <KeywordTag
        v-for="k in keywords"
        :key="k"
        :label="k"
        @remove="emit('remove-keyword', k)"
      />
    </div>

    <!-- 입력창 -->
    <div class="mt-10">
      <UserSettingInput
        :model-value="keyword"
        @update:model-value="(val) => emit('update:keyword', val)"
        placeholder="귀여운, 건방진, 까칠한, 겸손한"
        @submit="emit('submit')"
        :error="keywordError"
      />
      <FieldError :message="keywordError" />
    </div>
  </div>
</template>

<script setup>
import KeywordTag from '@/components/common/KeywordTag.vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import FieldError from '@/components/common/FieldError.vue'

const props = defineProps({
  keyword: String,
  keywordError: String,
  keywords: Array,
})

const emit = defineEmits(['update:keyword', 'submit', 'remove-keyword'])
</script>
