<script setup>
import { cn } from '@/lib/utils'

const emit = defineEmits(['update:modelValue', 'blur', 'focus', 'keydown'])
const props = defineProps({
  label: String,
  labelClass: {
    type: String,
    default: '',
  },
  class: {
    type: String,
    default: '',
  },
  placeholder: String,
  modelValue: String,
  type: {
    type: String,
    default: 'text',
  },
  error: String,
  disabled: Boolean,
  maxlength: String,
})
</script>

<template>
  <div class="w-full">
    <label v-if="label" :class="[cn('block mb-2 text-sm text-content-color', props.labelClass)]">{{
      label
    }}</label>
    <input
      :value="modelValue"
      :type="type"
      :placeholder="placeholder"
      :class="[
        {
          'error-input': error,
        },
        cn(
          'input bg-white w-full p-3 border-border-color border rounded-2xl placeholder-placeholder-color text-main-color h-12',
          props.class,
        ),
      ]"
      @input="emit('update:modelValue', $event.target.value)"
      @blur="emit('blur', $event)"
      @focus="emit('focus', $event)"
      @keydown="emit('keydown', $event)"
    />
  </div>
</template>

<style scoped>
.input {
  transition: all 0.2s ease;
}

.input:focus {
  outline: none;
  box-shadow: 0 0 0 2px theme('colors.focus-shadow-color');
}

.error-input {
  border-color: theme('colors.error-color');
}

.error-input:focus {
  border-color: theme('colors.error-color');
  box-shadow: 0 0 0 3px theme('colors.error-color/30%');
}
</style>
