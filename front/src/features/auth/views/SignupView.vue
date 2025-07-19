<template>
  <div class="main-container">
    <div class="sub-main-container !pb-8 justify-items-center">
      <div class="text-center mb-6">
        <h1 class="text-2xl font-bold text-main-color mb-2">회원가입</h1>
      </div>
      <!-- 회원가입 폼 -->
      <SignupForm class="mb-8"/>
      <!-- 약관 텍스트 -->
      <div class="px-6 mt-auto mb-8">
        <p class="text-xs text-gray-500 text-center leading-relaxed">
          해당 계정은
          <a href="/privacy" class="text-main-color underline">개인정보처리방침</a>으로 Rouby에서
          제공하는 서비스를 모두 이용하실 수 있습니다. 가입 시, 계정 및 서비스 이용약관,
          <a href="/privacy" class="text-main-color underline">개인정보처리방침</a>에 동의하는 것으로
          간주합니다.
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import SignupForm from '../components/SignupForm.vue'
</script>
