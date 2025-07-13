<template>
  <form @submit.prevent="onSubmit" class="p-6">
    <div class="space-y-6">
      <!-- 이메일 필드 -->
      <div class="space-y-2 gap-8">
        <div class="flex items-end gap-3">
          <BaseInput
            v-model="form.email"
            label="이메일"
            type="email"
            placeholder="your@email.com"
            :disabled="form.isVerificationStep"
            :error="errors.email"
            @blur="validateEmailField"
            label-class="text-auth-label-color"
            class="placeholder-placeholder-pink-color"
          />
          <BaseButton
            @click="requestVerification"
            class="max-w-24"
          >
            {{ loading.emailVerification ? '발송중…' : '인증하기' }}
          </BaseButton>
        </div>
        <FieldError :message="errors.email"/>
      </div>

      <!-- 인증번호 입력 단계 -->
      <div v-if="form.isVerificationStep" class="space-y-4 mt-0">
        <PinInput
          v-model="form.verificationCode"
          label="인증번호"
          helpText="인증 메일이 보이지 않는 경우, 스팸 메일함을 확인해주세요."
          :errorMessage="errors.verificationCode"
          :disabled="form.isEmailVerified"
          :length="6"
          @complete="verifyCode"
          label-class="text-auth-label-color"
        />
        <!-- 타이머 & 재전송 버튼 -->
        <div class="flex items-center justify-between text-sm">
          <p v-if="form.isEmailVerified" class="text-main-color">이메일 인증이 완료되었습니다.</p>
          <p v-else-if="timeLeft > 0" class="text-gray-500">{{ formatTime(timeLeft) }}</p>
          <button
            v-if="isResendAvailable && !form.isEmailVerified"
            type="button"
            @click="resendVerification"
            class="text-main-color hover:underline"
          >
            인증번호 재발송
          </button>
        </div>
      </div>

      <!-- 비밀번호 필드 -->
      <div class="space-y-2">
        <BaseInput
          v-model="form.password"
          label="비밀번호"
          type="password"
          placeholder="비밀번호를 입력하세요"
          :error="errors.password"
          @blur="validatePasswordField"
          label-class="text-auth-label-color"
          class="placeholder-placeholder-pink-color"
        />
        <p class="text-xs text-gray-500 mt-[-0.5rem](-8px) mx-2">영문 대·소문자/숫자/특수문자 중 2가지 이상 조합,
          8자~32자</p>
        <FieldError :message="errors.password"/>
      </div>

      <div class="space-y-2">
        <BaseInput
          v-model="form.passwordConfirm"
          label="비밀번호 확인"
          type="password"
          placeholder="비밀번호를 다시 입력하세요"
          :error="errors.passwordConfirm"
          @blur="validatePasswordConfirmField"
          label-class="text-auth-label-color"
          class="placeholder-placeholder-pink-color"
        />
        <FieldError :message="errors.passwordConfirm"/>
      </div>
    </div>

    <!-- 공백 -->
    <div class="h-8"></div>

    <!-- 가입하기 버튼 -->
    <div class="mt-8">
      <BaseButton
        type="submit"
        :disabled="loading.signup || !form.isEmailVerified"
        class="w-full font-medium transition-colors"
      >
        {{ loading.signup ? '가입중...' : '가입하기' }}
      </BaseButton>
    </div>

    <!-- 약관 텍스트 -->
    <div class="mt-4">
      <p class="text-xs text-gray-500 text-center leading-relaxed">
        해당 계정은
        <a href="/privacy" class="text-main-color underline">개인정보처리방침</a>으로 Rouby에서
        제공하는 서비스를 모두 이용하실 수 있습니다. 가입 시, 계정 및 서비스 이용약관,
        <a href="/privacy" class="text-main-color underline">개인정보처리방침</a>에 동의하는 것으로
        간주합니다.
      </p>
    </div>
  </form>
</template>

<script setup>
import {useSignupForm} from '../useSignupForm.js'
import BaseInput from '@/components/common/BaseInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import PinInput from '@/features/auth/components/PinInput.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const {
  form,
  errors,
  loading,
  timeLeft,
  isResendAvailable,
  requestVerification,
  verifyCode,
  onSubmit,
  resendVerification,
  validateEmailField,
  validatePasswordField,
  validatePasswordConfirmField,
  formatTime,
} = useSignupForm()
</script>
