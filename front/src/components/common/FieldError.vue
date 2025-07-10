<script setup>
defineProps({
  message: {
    type: String,
    default: '',
  },
})
</script>

<template>
  <p v-if="message" class="text-sm text-error-color mt-2 mx-2">
    {{ message }}
  </p>
</template>
