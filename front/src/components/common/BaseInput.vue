<script setup>
defineProps({
  label: String,
  placeholder: String,
  modelValue: String,
  type: {
    type: String,
    default: 'text',
  },
})

defineEmits(['update:modelValue'])
</script>

<template>
  <div class="input-group">
    <label class="input-label text-contentColor">{{ label }}</label>
    <input
      :type="type"
      :placeholder="placeholder"
      class="input placeholder-placeholderColor"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
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
  border-radius: 12px;
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
</style>
