<script setup>
import { defineProps, defineEmits } from 'vue'

const emit = defineEmits(['update:modelValue', 'blur', 'focus', 'keydown'])

defineProps({
  label: String,
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
  <div class="input-group">
    <label class="input-label text-contentColor">{{ label }}</label>
    <input
      v-bind="$attrs"
      :type="type"
      :placeholder="placeholder"
      :maxlength="maxlength"
      :disabled="disabled"
      class="input placeholder-placeholderColor h-12"
      :class="{
        'error-input': error,
      }"
      @input="emit('update:modelValue', $event.target.value)"
      @blur="emit('blur', $event)"
      @focus="emit('focus', $event)"
      @keydown="emit('keydown', $event)"
      :value="modelValue"
    />
  </div>
</template>

<style scoped>
.input-group {
  width: 100%;
  margin-bottom: 24px;
}

.input-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
}

.input {
  width: 100%;
  padding: 16px;
  border: 2px solid theme('colors.border-color');
  border-radius: 16px;
  font-size: 16px;
  background: white;
  transition: all 0.2s ease;
  color: theme('colors.text-color');
}

.input:focus {
  outline: none;
  border-color: theme('colors.focus-border-color');
  box-shadow: 0 0 0 3px theme('colors.focus-shadow-color');
}


.error-input {
  border-color: theme('colors.error-color');
}

.error-input:focus {
  border-color: theme('colors.error-color');
  box-shadow: 0 0 0 3px theme('colors.error-color/30%');
}

</style>
