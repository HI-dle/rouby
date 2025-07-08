<script setup>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
</script>

<template>
  <Header class="bg-inherit" />
  <router-view class="wrapper" />
  <Footer />
</template>
