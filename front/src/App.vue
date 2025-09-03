<script setup>
import { RouterView } from 'vue-router'
import { initVH } from './layouts/layout'
import { onBeforeUnmount, onMounted } from 'vue'
import ToastCard from './components/common/ToastCard.vue'

onMounted(() => {
  initVH()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', setVH)
})
</script>

<template>
  <RouterView />
  <ToastCard />
</template>

<style scoped></style>
