<script setup>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
</script>

<template>
  <Header class="bg-inherit" />
  <div class="wrapper">
    <router-view />
  </div>
  <Footer />
</template>
