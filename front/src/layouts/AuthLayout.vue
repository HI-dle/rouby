<script setup>
import Header from '@/components/Header.vue'
import { useActivaPath } from '@/shared/composable/useActivePath'

const { isActive } = useActivaPath()
</script>

<template>
  <div class="wrapper flex flex-col h-[calc(var(--vh,1vh)_*100)] overflow-hidden">
    <Header v-if="isActive('/login')" class="shrink-0" />
    <main class="flex flex-col max-w-md w-full mx-auto min-h-0">
      <router-view />
    </main>
  </div>
</template>
