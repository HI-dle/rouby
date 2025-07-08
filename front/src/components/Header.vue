<script setup>
import HeaderIcon from '@/assets/header_logo.svg'
</script>

<template>
  <header class="flex p-4 justify-between w-full max-w-md mx-auto">
    <figure class="m-0">
      <img :src="HeaderIcon" alt="루비 헤더 로고" class="h-5 object-contain" />
    </figure>
  </header>
</template>
