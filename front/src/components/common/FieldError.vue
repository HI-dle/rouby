<script setup>
import { cn } from '@/lib/utils'

const props = defineProps({
  message: {
    type: String,
    default: '',
  },
  class: {
    type: String,
    default: '',
  },
})
</script>

<template>
  <p v-if="message.trim().length" :class="[cn('text-sm text-error-color mt-2 mx-2', props.class)]">
    {{ message }}
  </p>
</template>
