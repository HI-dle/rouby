<script setup>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
</script>

<template>
  <div class="wrapper flex flex-col h-[calc(var(--vh,1vh)_*100)] overflow-hidden">
    <Header class="shrink-0" />
    <main class="flex flex-col lg:max-w-screen-md w-full mx-auto min-h-0">
      <router-view />
    </main>
    <Footer class="shrink-0" />
  </div>
</template>
