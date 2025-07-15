<template>
  <div class="main-container">
    <div class="sub-main-container !pb-8">
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
      <ResetPasswordForm />
    </div>
  </div>
</template>

<script setup>
import ResetPasswordForm from '@/features/auth/components/ResetPasswordForm.vue'
import PasswordLockIcon from '@/assets/password_lock.svg'
</script>
