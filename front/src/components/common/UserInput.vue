<script setup>
import { ref } from 'vue'
import { cn } from '@/lib/utils'
import SettingButton from '@/assets/settingButton.svg'

const emit = defineEmits([
  'update:modelValue',
  'submit',
  'blur',
  'focus',
  'keydown',
])

const props = defineProps({
  maxlength: Number,
  modelValue: String,
  placeholder: String,
  class: String,
  disabled: Boolean,
  error: {
    type: String,
    default: '',
  },
  label: String,
  labelClass: {
    type: String,
    default: '',
  },
  inputClass: {
    type: String,
    default: '',
  },
})

const isFocused = ref(false)

const handleFocus = (e) => {
  isFocused.value = true
  emit('focus', e)
}
const handleBlur = (e) => {
  isFocused.value = false
  emit('blur', e)
}
</script>

<template>
  <div :class="cn('w-full', props.class)">
    <!-- 라벨 -->
    <label
      v-if="label"
      :class="cn('block mb-2 text-sm text-content-color', labelClass)"
    >
      {{ label }}
    </label>

    <!-- input + 버튼 -->
    <div class="relative">
      <textarea
        :maxlength="maxlength ?? 100"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :class="
          cn(
            'w-full h-10 pl-4 pr-10 py-1.5 rounded-2xl bg-white border text-main-color placeholder:text-placeholder-color text-base outline-none focus:ring-1 transition shadow-sm resize-none',
            error
              ? 'border-error-color'
              : 'border-border-color focus:ring-[#B6A6FF]',
            inputClass,
          )
        "
        :style="
          error && isFocused
            ? 'box-shadow: 0 0 0 1px rgba(255, 72, 66, 0.3)'
            : ''
        "
        @input="emit('update:modelValue', $event.target.value)"
        @keyup.enter="emit('submit', $event)"
        @keydown="emit('keydown', $event)"
        @blur="handleBlur"
        @focus="handleFocus"
      />
      <button
        type="button"
        @click="emit('submit', $event)"
        class="absolute right-3 bottom-1.5 -translate-y-1/2 w-5 h-5 p-0"
      >
        <img :src="SettingButton" alt="Submit" class="w-full h-full" />
      </button>
    </div>
  </div>
</template>
