<script setup>
import { computed } from 'vue'
import { parseMdToHtmlAndSanitize } from '@/shared/utils/htmlContentUtils'

const { loading, error, content } = defineProps({
  loading: { type: Boolean, required: true },
  error: { type: String, default: '' },
  content: { type: String, default: '' },
})
const sanitizedHtml = computed(() => parseMdToHtmlAndSanitize(content))
</script>

<template>
  <div v-if="!error && !loading">
    <h2 class="text-center text-lg font-bold mb-2 mt-2">
      ✨ 루비의 오늘 시작 브리핑 ✨
    </h2>
    <div
      class="mt-6 p-6 rounded-xl bg-gradient-to-r from-[#5a63d8] to-[#693f99] text-white shadow-lg max-w-xl mx-auto"
    >
      <div
        class="overflow-y-auto max-h-150 whitespace-pre-wrap"
        v-html="sanitizedHtml"
      ></div>
    </div>
  </div>
</template>
