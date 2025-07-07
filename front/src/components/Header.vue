<script setup>
  import HeaderIcon from '@/assets/header_logo.svg'
</script>

<template>
  <div class="header">
    <div class="brand">
      <figure>
        <img :src="HeaderIcon" alt="헤더 아이콘" >
      </figure>
    </div>
  </div>
</template>

<style scoped>
.header {
  max-width: 375px;
  display: flex;
  padding: 16px;
  background-color: white;
}

.brand img {
  height: 20px;
  object-fit: contain;
}

.brand figure {
  margin: 0;
}
</style>
