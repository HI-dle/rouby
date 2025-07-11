<template>
  <div class="flex justify-center space-x-2">
    <input
      v-for="(digit, idx) in digits"
      :key="idx"
      ref="inputs"
      type="text"
      inputmode="numeric"
      maxlength="1"
      class="w-12 h-12 text-center text-xl border border-borderColor rounded-lg focus:outline-none focus:border-focusBorderColor transition-colors"
      :class="{
        'border-red-500': error,
        'bg-gray-100': disabled
      }"
      :disabled="disabled"
      :value="digit"
      @input="onInput($event, idx)"
      @keydown="onKeydown($event, idx)"
    />
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'

const props = defineProps({
  modelValue: { type: String, required: true },
  length:     { type: Number, default: 6 },
  disabled:   { type: Boolean, default: false },
  error:      { type: Boolean, default: false }
})
const emit = defineEmits(['update:modelValue'])

const inputs = ref([])
const digits = ref(Array(props.length).fill(''))

// modelValue → digits 동기화
watch(() => props.modelValue, val => {
  digits.value = val.padEnd(props.length, ' ').substr(0, props.length).split('')
})

// 인덱스 idx 위치에 입력되면 modelValue 업데이트
function onInput(e, idx) {
  const ch = e.target.value.replace(/\D/, '').charAt(0) || ''
  digits.value[idx] = ch
  const newVal = digits.value.join('').trimEnd()
  emit('update:modelValue', newVal)
  if (ch && idx < props.length - 1) {
    // 다음 칸으로 포커스
    nextTick(() => inputs.value[idx + 1]?.focus())
  }
}

// 백스페이스로 지울 때 이전 칸으로 이동
function onKeydown(e, idx) {
  if (e.key === 'Backspace' && !digits.value[idx] && idx > 0) {
    nextTick(() => inputs.value[idx - 1]?.focus())
  }
}
</script>

<style scoped>
/* 여러분의 Tailwind나 CSS 변수에 맞춰 조정하세요 */
</style>
