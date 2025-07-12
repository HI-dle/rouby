<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="text-center">
        <h1 class="text-2xl font-bold text-main-color mb-2">비밀번호 변경</h1>
      </div>
      <!-- Lock Icon -->
      <div class="lock-container flex justify-center items-center my-6">
        <figure>
          <img :src="PasswordLockIcon" alt="패스워드 잠금 아이콘" />
        </figure>
      </div>
      <!-- 비밀번호 변경 폼 -->
      <PasswordForm />
    </div>
  </div>
</template>

<script setup>
import PasswordForm from '@/features/auth/components/PasswordForm.vue'
import PasswordLockIcon from '@/assets/password_lock.svg'
</script>
