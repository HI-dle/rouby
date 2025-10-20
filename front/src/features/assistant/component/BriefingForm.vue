<script setup>
const { loading, error, content } = defineProps({
  loading: { type: Boolean, required: true },
  error: { type: String, default: '' },
  htmlContent: { type: String, default: '' },
})
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
        v-html="htmlContent"
      ></div>
    </div>
  </div>
</template>
