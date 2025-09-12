<template>
  <form @submit.prevent="$emit('submit')" class="space-y-4">
    <!-- 이메일 입력 -->
    <div>
      <BaseInput
        v-model="localEmail"
        label="이메일"
        type="email"
        placeholder="your@email.com"
        :error="emailError"
        @blur="$emit('validate-email')"
        label-class="text-auth-label-color"
        class="placeholder-placeholder-pink-color"
      />
      <FieldError :message="emailError" />
    </div>

    <!-- 비밀번호 입력 -->
    <div>
      <BaseInput
        v-model="localPassword"
        label="비밀번호"
        type="password"
        placeholder="••••••••"
        :error="passwordError"
        @blur="$emit('validate-password')"
        label-class="text-auth-label-color"
        class="placeholder-placeholder-pink-color"
      />
      <FieldError :message="passwordError" />
    </div>

    <!-- 로그인 상태 유지 -->
    <label
      class="inline-flex items-center ml-2 mb-4 cursor-pointer select-none"
    >
      <input
        type="checkbox"
        v-model="localStaySignedIn"
        class="accent-violet-600"
      />
      <span class="ml-2 text-sm text-indigo-600">로그인 상태 유지</span>
    </label>

    <!-- 에러 메시지 -->
    <p v-if="loginError" class="text-sm text-red-500 text-center">
      {{ loginError }}
    </p>

    <BaseButton type="submit" class="w-full font-medium"> 로그인 </BaseButton>

    <!-- 구분선 및 소셜 -->
    <div class="flex items-center w-full text-sm text-gray-500">
      <hr class="flex-grow border-t border-gray-200" />
      <span class="px-3 text-main-color">또는</span>
      <hr class="flex-grow border-t border-gray-200" />
    </div>

    <div class="flex justify-between gap-4 mb-6">
      <SocialLoginButton
        :icon="kakaoIcon"
        alt="Kakao Login"
        @click="$emit('kakao')"
      />
      <SocialLoginButton
        :icon="googleIcon"
        alt="Google Login"
        @click="$emit('google')"
      />
      <SocialLoginButton
        :icon="appleIcon"
        alt="Apple Login"
        @click="$emit('apple')"
      />
    </div>
  </form>
</template>

<script setup>
import { computed, defineProps, defineEmits } from 'vue'
import BaseInput from '@/components/common/BaseInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import SocialLoginButton from './SocialLoginButton.vue'
import kakaoIcon from '@/assets/kakao.svg'
import googleIcon from '@/assets/google.svg'
import appleIcon from '@/assets/apple.svg'

const props = defineProps({
  email: String,
  password: String,
  staySignedIn: Boolean,
  emailError: String,
  passwordError: String,
  loginError: String,
})

const emit = defineEmits([
  'update:email',
  'update:password',
  'update:staySignedIn',
  'validate-email',
  'validate-password',
  'submit',
  'kakao',
  'google',
  'apple',
])

// ✅ computed getter/setter로 양방향 바인딩
const localEmail = computed({
  get: () => props.email,
  set: (val) => emit('update:email', val),
})

const localPassword = computed({
  get: () => props.password,
  set: (val) => emit('update:password', val),
})

const localStaySignedIn = computed({
  get: () => props.staySignedIn,
  set: (val) => emit('update:staySignedIn', val),
})
</script>
