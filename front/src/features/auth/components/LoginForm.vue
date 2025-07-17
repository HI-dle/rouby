<template>
  <form @submit.prevent="onLogin" class="space-y-4">
    <!-- 이메일 입력 -->
    <div>
      <BaseInput
        v-model="email"
        label="이메일"
        type="email"
        placeholder="your@email.com"
        :error="emailError"
        @blur="validateEmail"
        label-class="text-auth-label-color"
        class="placeholder-placeholder-pink-color"
      />
      <FieldError :message="emailError" />
    </div>

    <!-- 비밀번호 입력 -->
    <div>
      <BaseInput
        v-model="password"
        label="비밀번호"
        type="password"
        placeholder="••••••••"
        :error="passwordError"
        @blur="validatePassword"
        label-class="text-auth-label-color"
        class="placeholder-placeholder-pink-color"
      />
      <FieldError :message="passwordError" />
    </div>

    <!-- 로그인 상태 유지 체크박스 -->
    <label class="inline-flex items-center ml-2 mb-4 cursor-pointer select-none">
      <input type="checkbox" v-model="staySignedIn" class="accent-violet-600" />
      <span class="ml-2 text-sm text-indigo-600">로그인 상태 유지</span>
    </label>

    <!-- 에러 메시지 -->
    <p v-if="loginError" class="text-sm text-red-500 text-center">
      {{ loginError }}
    </p>

    <!-- 로그인 버튼 -->
    <BaseButton type="submit" class="w-full font-medium"> 로그인 </BaseButton>

    <!-- 구분선 -->
    <div class="flex items-center w-full text-sm text-gray-500">
      <hr class="flex-grow border-t border-gray-200" />
      <span class="px-3 text-main-color">또는</span>
      <hr class="flex-grow border-t border-gray-200" />
    </div>

    <div class="flex justify-between gap-4 mb-6">
      <SocialLoginButton :icon="kakaoIcon" alt="Kakao Login" @click="onKakaoLogin" />
      <SocialLoginButton :icon="googleIcon" alt="Google Login" @click="onGoogleLogin" />
      <SocialLoginButton :icon="appleIcon" alt="Apple Login" @click="onAppleLogin" />
    </div>
  </form>
</template>

<script setup>
import { useLoginForm } from '../useLoginForm.js'
import BaseInput from '@/components/common/BaseInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import SocialLoginButton from './SocialLoginButton.vue'
import kakaoIcon from '@/assets/kakao.svg'
import googleIcon from '@/assets/google.svg'
import appleIcon from '@/assets/apple.svg'

const {
  email,
  password,
  staySignedIn,
  emailError,
  passwordError,
  loginError,
  validateEmail,
  validatePassword,
  onLogin,
} = useLoginForm()
</script>
