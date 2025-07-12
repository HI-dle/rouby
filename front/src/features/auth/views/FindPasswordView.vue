<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="text-center">
        <h1 class="text-2xl font-bold text-main-color mb-2">비밀번호 찾기</h1>
      </div>
      <!-- Lock Icon -->
      <div class="lock-container flex justify-center items-center my-6">
        <figure>
          <img :src="PasswordKeyIcon" alt="패스워드 키 아이콘" />
        </figure>
      </div>
      <!-- 비밀번호 찾기 폼 -->
      <FindPasswordForm />
    </div>
  </div>
</template>

<script setup>
import FindPasswordForm from '../components/FindPasswordForm.vue'
import PasswordKeyIcon from '@/assets/password_key.svg'
</script>
