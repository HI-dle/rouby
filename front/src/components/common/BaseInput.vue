<script setup>
import { defineProps, defineEmits } from 'vue'

const emit = defineEmits(['update:modelValue', 'blur', 'focus', 'keydown'])

defineProps({
  label: String,
  labelClass: {
    type: String,
    default: '',
  },
  placeholder: String,
  modelValue: String,
  type: {
    type: String,
    default: 'text'
  },
  error: String,
  disabled: Boolean,
  maxlength: String
})

</script>

<template>
  <div class="w-full mb-6">
    <label
      v-if="label"
      class="block mb-2 text-sm text-content-color"
      :class="labelClass"
    >{{ label }}</label>
    <input
      :value="modelValue"
      :type="type"
      :placeholder="placeholder"
      class="input bg-white w-full p-3 border-border-color border rounded-[16px] placeholder-placeholderColor text-main-color h-12"
      :class="{
        'error-input': error,
      }"
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
