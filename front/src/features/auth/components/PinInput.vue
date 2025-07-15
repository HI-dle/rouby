<template>
  <div class="input-group">
    <!-- 레이블 -->
    <label v-if="label" class="block text-sm font-medium text-contentColor mb-1"
    :class="labelClass"
    >
      {{ label }}
    </label>
    <!-- 도움말 텍스트 -->
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
        @focus="(e) => onFocus(e, idx)"
        @paste="(e) => onPaste(e, idx)"
        class="aspect-square w-[clamp(2.5rem,10vw,3rem)] text-center text-xl border rounded-lg transition-colors"
        :class="{
          'border-gray-300 focus:border-focus-border-color focus:shadow-focus-shadow-color': !errorMessage,
          'border-error-color focus:shadow-error-color': !!errorMessage,
          'bg-gray-100': disabled,
          'rounded-l-2xl': idx === 0,
          'rounded-r-2xl': idx === length - 1,
          'ring-2 ring-blue-400': idx === activeIndex,
          'text-content-color font-medium': digits[idx],
        }"
      />
    </div>

    <FieldError :message="errorMessage" />
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import FieldError from '@/components/common/FieldError.vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  length: { type: Number, default: 6 },
  disabled: { type: Boolean, default: false },
  errorMessage: { type: String, default: '' },
  label: { type: String, default: '' },
  labelClass: { type: String, default: '' },
  helpText: { type: String, default: '' }
})

const emit = defineEmits(['update:modelValue', 'blur', 'focus', 'keydown', 'complete'])

const inputs = ref([])
const digits = ref(Array(props.length).fill(''))

onMounted(() => {
  const chars = props.modelValue.split('')
  digits.value = digits.value.map((_, i) => chars[i] || '')
})

function onInput(e, idx) {
  const input = e.target
  const raw = input.value
  const ch = raw.replace(/[^A-Za-z0-9]/g, '').charAt(0) || ''

  if (!ch) {
    return
  }

  digits.value[idx] = ch

  input.value = ch

  if (idx < props.length - 1) {
    nextTick(() => inputs.value[idx + 1]?.focus())
  }

  emit('update:modelValue', digits.value.join(''))

  if (digits.value.every(d => d)) {
    emit('complete')
  }
}

function onKeydown(e, idx) {
  emit('keydown', e)
  const isPaste =
    (e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'v'
  if (isPaste) return

  const isChar = /^[a-zA-Z0-9]$/.test(e.key)
  if (isChar) {
    digits.value[idx] = e.key
    emit('update:modelValue', digits.value.join(''))

    if (idx < props.length - 1) {
      nextTick(() => inputs.value[idx + 1]?.focus())
    }

    if (digits.value.every(d => d)) {
      emit('complete')
    }

    e.preventDefault()
    return
  }

  if (e.key === 'Backspace') {
    e.preventDefault()

    if (digits.value[idx]) {
      digits.value[idx] = ''
      emit('update:modelValue', digits.value.join(''))
    } else if (idx > 0) {
      digits.value[idx - 1] = ''
      emit('update:modelValue', digits.value.join(''))
      nextTick(() => inputs.value[idx - 1]?.focus())
    }
    return
  }

  if (e.key === 'ArrowLeft' && idx > 0) {
    nextTick(() => inputs.value[idx - 1]?.focus())
    return
  }

  if (e.key === 'ArrowRight' && idx < props.length - 1) {
    nextTick(() => inputs.value[idx + 1]?.focus())
    return
  }
}

function onPaste(e, idx) {
  const pasted = e.clipboardData.getData('text')
  .replace(/[^A-Za-z0-9]/g, '')
  .slice(0, props.length - idx) // 뒤로 남은 칸만큼만

  pasted.split('').forEach((char, i) => {
    digits.value[idx + i] = char
  })

  emit('update:modelValue', digits.value.join(''))

  if (digits.value.every(d => d)) {
    emit('complete')
  }

  nextTick(() => {
    inputs.value[Math.min(idx + pasted.length, props.length - 1)]?.focus()
  })

  e.preventDefault()
}

const activeIndex = ref(-1)

function onFocus(e, idx) {
  activeIndex.value = idx

  nextTick(() => {
    const el = e.target
    const len = el.value.length
    el.setSelectionRange(len, len)
  })

  emit('focus', e)
}


// function onFocus(e) {
//   emit('focus', e)
// }

function onBlur(e) {
  activeIndex.value = -1
  emit('blur', e)
}

</script>


<style scoped>
.focus\:shadow-blue-100:focus {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.focus\:shadow-red-100:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.2);
}

input {
  caret-color: transparent;
}

</style>
