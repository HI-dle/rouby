<!-- src/components/common/BaseButton.vue -->
<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: 'button'
  },
  disabled: {
    type: Boolean,
    default: false
  },
})

const emit = defineEmits(['click'])
</script>

<template>
  <button
    :type="type"
    :disabled="disabled"
    @click="emit('click', $event)"
    :class="[
      'px-4',
      'h-12',
      `bg-gradient-to-r from-[#667EEA] to-[#764BA2]`,
      'text-white',
      'rounded-[16px]',
      'hover:opacity-90',
      'disabled:opacity-50',
      'whitespace-nowrap',
    ]"
  >
    <slot />
  </button>
</template>
