<template>
  <div class="input-group">
    <!-- 1) 레이블 -->
    <label v-if="label" class="block text-sm font-medium text-contentColor mb-1"
    :class="labelClass"
    >
      {{ label }}
    </label>
    <!-- 2) 도움말 텍스트 -->
    <p v-if="helpText" class="text-xs text-gray-500 mb-2">
      {{ helpText }}
    </p>

    <div class="flex justify-center space-x-2">
      <input
        v-for="(_, idx) in length"
        :key="idx"
        ref="inputs"
        type="text"
        inputmode="text"
        maxlength="1"
        pattern="[A-Za-z0-9]*"
        :disabled="disabled"
        :value="digits[idx]"
        @input="onInput($event, idx)"
        @keydown="onKeydown($event, idx)"
        @blur="onBlur"
        @focus="onFocus"
        class="w-12 h-12 text-center text-xl border-2 rounded-lg transition-colors"
        :class="{
          'border-gray-300 focus:border-blue-500 focus:shadow-blue-100': !errorMessage,
          'border-red-500 focus:border-red-500 focus:shadow-red-100': !!errorMessage,
          'bg-gray-100': disabled,
          'rounded-l-2xl': idx === 0,
          'rounded-r-2xl': idx === length - 1,
        }"
      />
    </div>

    <!-- 4) 에러 메시지 -->
    <FieldError :message="errorMessage" />
  </div>
</template>

<style scoped>
.focus\:shadow-blue-100:focus {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.focus\:shadow-red-100:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.2);
}
</style>

<script setup>
import { ref, watch, nextTick, onMounted } from 'vue'
import FieldError from '@/components/common/FieldError.vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  length: { type: Number, default: 6 },
  disabled: { type: Boolean, default: false },
  errorMessage: { type: String, default: '' },   // 에러 텍스트 받기
  label: { type: String, default: '' },
  labelClass: {
    type: String,
    default: ''
  },
  helpText: { type: String, default: '' }
})

const emit = defineEmits([
  'update:modelValue',
  'blur',
  'focus',
  'keydown',
  'complete'
])

// 각 칸의 DOM ref
const inputs = ref([])
// 내부 상태: 각 칸에 표시할 숫자
const digits = ref(Array(props.length).fill(''))

// 부모 modelValue가 바뀌면 digits 동기화
watch(
  () => props.modelValue,
  val => {
    const chars = val.split('')
    digits.value = Array(props.length)
    .fill('')
    .map((_, i) => chars[i] || '')
  },
  { immediate: true }
)

onMounted(() => {
  const chars = props.modelValue.split('')
  digits.value = Array(props.length)
  .fill('')
  .map((_, i) => chars[i] || '')
})

function onInput(e, idx) {
  const ch = e.target.value
  .replace(/[^A-Za-z0-9]/g, '')
  .charAt(0) || ''
  digits.value[idx] = ch
  const newVal = digits.value.join('')
  emit('update:modelValue', newVal)

  if (ch && idx < props.length - 1) {
    nextTick(() => inputs.value[idx + 1]?.focus())
  }
  if (newVal.length === props.length) {
    emit('complete')
  }
}

function onKeydown(e, idx) {
  emit('keydown', e)

  if (e.key === 'Backspace') {
    if (!digits.value[idx] && idx > 0) {
      nextTick(() => inputs.value[idx - 1]?.focus())
    } else {
      digits.value[idx] = ''
      emit('update:modelValue', digits.value.join(''))
    }
  }
}

function onBlur(e) {
  emit('blur', e)
}

function onFocus(e) {
  emit('focus', e)
}
</script>
