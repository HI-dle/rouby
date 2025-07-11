<template>
  <form @submit.prevent="onSubmit" class="p-6 space-y-6">
    <!-- 이메일 필드 -->
    <div class="space-y-2">
      <div class="flex items-center gap-2">
        <BaseInput
            v-model="form.email"
            label="이메일"
            type="email"
            placeholder="your@email.com"
            :disabled="form.isVerificationStep"
            :error="errors.email"
            @input="validateEmailField"
        />
        <BaseButton @click="requestVerification">
          {{ loading.emailVerification ? '발송중…' : '인증하기' }}
        </BaseButton>
      </div>
      <FieldError :message="errors.email"/>
    </div>

    <!-- 인증번호 확인 단계 -->
    <div v-if="hasRequestedVerification" class="space-y-4">
      <!-- 알림 메시지들 -->
      <div class="space-y-1 text-sm">
        <FieldError :message="errors.email" v-if="errors.email"/>
        <p v-else class="text-main-color">인증번호를 이메일로 전송하였습니다.</p>
      </div>

      <!-- 인증번호 확인 -->
      <div class="space-y-2">
        <label class="block text-sm font-medium text-contentColor">인증번호 확인</label>
        <p class="text-sm text-gray-600">
          인증 메일이 보이지 않는 경우, 스팸 메일함을 확인해주세요.
        </p>

        <PinInput
            v-model="form.verificationCode"
            :disabled="form.isEmailVerified"
            :error="!!errors.verificationCode"
            length=6
        />
        <FieldError :message="errors.verificationCode"/>

        <!-- 타이머 및 재전송 버튼 -->
        <div class="flex items-center justify-between text-sm">
          <div class="flex items-center justify-between text-sm">
            <p v-if="form.isEmailVerified" class="text-green-500">이메일 인증이 완료되었습니다.</p>
            <p v-else-if="timeLeft > 0" class="text-gray-500">
              {{ formatTime(timeLeft) }}
            </p>
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
      </div>
    </div>

    <!-- 비밀번호 필드들 -->
    <div class="space-y-2">
      <BaseInput
          v-model="form.password"
          label="비밀번호"
          type="password"
          placeholder="비밀번호를 입력하세요"
          :error="errors.password"
          @input="validatePasswordField"
      />
      <p class="text-xs text-gray-500">영문 대·소문자/숫자/특수문자 중 2가지 이상 조합, 8자~32자</p>
      <FieldError :message="errors.password"/>
    </div>

    <div class="space-y-2">
      <BaseInput
          v-model="form.passwordConfirm"
          label="비밀번호 확인"
          type="password"
          placeholder="비밀번호를 다시 입력하세요"
          :error="errors.passwordConfirm"
          @input="validatePasswordConfirmField"
      />
      <FieldError :message="errors.passwordConfirm"/>
    </div>

    <!-- 가입하기 버튼 -->
    <BaseButton
        type="submit"
        :disabled="loading.signup || !form.isEmailVerified"
        heightClass="h-12"
        customClass="w-full font-medium transition-colors"
    >
      {{ loading.signup ? '가입중...' : '가입하기' }}
    </BaseButton>
    <!-- 개인정보 처리방침 -->
    <p class="text-xs text-gray-500 text-center leading-relaxed">
      해당 계정은
      <a href="/privacy" class="text-main-color underline">개인정보처리방침</a>으로 Rouby에서
      제공하는 서비스를 모두 이용하실 수 있습니다. 가입 시, 계정 및 서비스 이용약관,
      <a href="/privacy" class="text-main-color underline">개인정보 처리방침</a>에 동의하는 것으로
      간주합니다.
    </p>
  </form>
</template>

<script setup>
import {useSignupForm} from '../useSignupForm.js'
import BaseInput from '@/components/common/BaseInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import PinInput from '@/features/auth/components/PinInput.vue'
import BaseButton from "@/components/common/BaseButton.vue";

const {
  form,
  errors,
  loading,
  hasRequestedVerification,
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
