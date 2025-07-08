<script setup>
defineProps({
  modelValue: Boolean,
})
defineEmits(['update:modelValue'])
</script>

<template>
  <label class="inline-flex items-center cursor-pointer relative">
    <input
      type="checkbox"
      class="sr-only peer"
      :checked="modelValue"
      @change="$emit('update:modelValue', $event.target.checked)"
    />
    <div
      class="w-11 h-6 bg-gray-300 rounded-full peer peer-checked:bg-bodyText transition-all"
    ></div>
    <div
      class="absolute ml-1 top-[4px] w-4 h-4 bg-white rounded-full shadow transition-transform peer-checked:translate-x-full"
    ></div>
  </label>
</template>
