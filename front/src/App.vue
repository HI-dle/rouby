<script setup>
import { RouterView } from 'vue-router'
import { initVH } from './layouts/layout'
import { onBeforeUnmount, onMounted } from 'vue'

onMounted(() => {
  initVH()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', setVH)
})
</script>

<template>
  <RouterView />
</template>

<style scoped></style>
