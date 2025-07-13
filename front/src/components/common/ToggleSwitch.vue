<script setup>
import { cn } from '@/lib/utils'

const props = defineProps({
  modelValue: Boolean,
  wrapperClass: { type: String, default: '' },
  trackClass: { type: String, default: '' },
  thumbClass: { type: String, default: '' },
})

defineEmits(['update:modelValue'])
</script>

<template>
  <label :class="['inline-flex items-center cursor-pointer relative', props.wrapperClass]">
    <input
      type="checkbox"
      class="sr-only peer"
      :checked="modelValue"
      @change="$emit('update:modelValue', $event.target.checked)"
    />
    <div
      :class="[
        cn(
          'w-11 h-6 rounded-full bg-gray-300 peer peer-checked:bg-main-color transition-all',
          props.trackClass,
        ),
      ]"
    ></div>
    <div
      :class="[
        cn(
          'absolute ml-1 top-[4px] w-4 h-4 bg-white rounded-full shadow transition-transform peer-checked:translate-x-full',
          props.thumbClass,
        ),
      ]"
    ></div>
  </label>
</template>
