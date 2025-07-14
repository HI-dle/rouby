<template>
  <div class="main-container">
    <div class="sub-main-container !pb-8 justify-center">
      <!-- 로고 -->
      <div class="text-center my-6">
        <img :src="HeaderIcon" alt="Rouby Logo" class="mx-auto h-10 w-auto" />
        <p class="text-sm text-violet-500 mt-1">나만의 루틴 비서, 루비를 만나보세요!</p>
      </div>

      <!-- 환영 문구 -->
      <div class="text-center mb-8">
        <h2 class="text-2xl font-bold text-gray-500">Welcome Back</h2>
        <p class="text-sm text-indigo-500 mt-1 leading-relaxed">
          보석처럼 빛나는 당신의 오늘<br />루비가 언제나 함께합니다.
        </p>
      </div>

      <!-- 로그인 폼 -->
      <LoginForm />

      <!-- 하단 링크 -->
      <div class="text-center text-xs space-x-2 mt-8 mb-6">
        <a href="#" class="text-main-color hover:underline">비밀번호 찾기</a>
        <span>|</span>
        <a href="#" class="text-main-color hover:underline">아이디 찾기</a>
        <span>|</span>
        <RouterLink to="signup" class="text-main-color hover:underline">회원가입</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import LoginForm from '../components/LoginForm.vue'
import HeaderIcon from '@/assets/header_logo.svg'
</script>
