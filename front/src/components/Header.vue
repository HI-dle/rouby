<script setup>
import HeaderIcon from '@/assets/header_logo.svg'
</script>

<template>
  <header class="flex px-4 py-3 justify-between w-full lg:max-w-screen-md mx-auto">
    <RouterLink to="/routine" class="flex justify-center items-center">
      <figure class="m-0">
        <img :src="HeaderIcon" alt="루비 헤더 로고" class="h-5 object-contain" />
      </figure>
    </RouterLink>
  </header>
</template>
