<script setup>
import { ref } from 'vue'
import { cn } from '@/lib/utils'
import SettingButton from '@/assets/settingButton.svg'

const emit = defineEmits(['update:modelValue', 'submit', 'blur', 'focus', 'keydown'])

const props = defineProps({
  modelValue: String,
  placeholder: String,
  class: String,
  disabled: Boolean,
  error: String,
  label: String,
  labelClass: {
    type: String,
    default: ''
  }
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
      <input
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :class="cn(
          'w-full h-10 pl-4 pr-10 rounded-2xl bg-white border text-main-color placeholder:text-placeholder-color text-base outline-none focus:ring-2 transition shadow-sm',
          error
            ? 'border-error-color'
            : 'border-border-color focus:ring-[#B6A6FF]'
        )"
        :style="error && isFocused ? 'box-shadow: 0 0 0 3px rgba(255, 72, 66, 0.3)' : ''"
        @input="emit('update:modelValue', $event.target.value)"
        @keyup.enter="emit('submit')"
        @keydown="emit('keydown', $event)"
        @blur="handleBlur"
        @focus="handleFocus"
      />
      <button
        type="button"
        @click="emit('submit')"
        class="absolute right-3 top-1/2 -translate-y-1/2 w-6 h-6 p-0"
      >
        <img :src="SettingButton" alt="Submit" class="w-full h-full" />
      </button>
    </div>
  </div>
</template>
