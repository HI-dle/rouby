<!-- components/common/KeywordTag.vue -->
<template>
  <div
    v-if="isValid"
    class="relative inline-flex items-center bg-[#677BE61A] text-main-color text-sm font-bold px-4 py-2 rounded-full"
  >
    <span>#{{ label }}</span>

    <button
      @click="emit('remove')"
      class="absolute -top-1 -right-1 w-4 h-4 rounded-full bg-main-color flex items-center justify-center text-white hover:bg-indigo-600 transition"
    >
      <X class="w-3 h-3" />
    </button>
  </div>
</template>

<script setup>
import { X } from 'lucide-vue-next'
import { computed } from 'vue'
import { isValidKeyword } from '@/shared/composable/useKeywordValidator'

const props = defineProps({
  label: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(['remove'])

// 공통 유효성 검사 함수 사용
const isValid = computed(() => isValidKeyword(props.label).valid)
</script>
