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
  <div class="w-full mb-6">
    <label class="block mb-2 text-sm text-contentColor">{{ label }}</label>
    <input
      :type="type"
      :placeholder="placeholder"
      class="input bg-white w-full p-3 border-border-color border rounded-xl placeholder-placeholderColor text-main-color"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
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
</style>
