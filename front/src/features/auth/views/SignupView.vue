<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="text-center">
        <h1 class="text-2xl font-bold text-main-color mb-2">회원가입</h1>
      </div>

      <!-- 회원가입 폼 -->
      <SignupForm />
      <!-- 로그인 링크 -->
      <div class="text-center pb-8">
        <p class="text-sm text-gray-600">
          이미 계정이 있으신가요?
          <RouterLink to="/login" class="text-main-color font-medium hover:underline">
            로그인하기
          </RouterLink>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import SignupForm from '../components/SignupForm.vue'
</script>
