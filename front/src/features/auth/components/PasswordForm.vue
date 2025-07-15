<template>
  <!-- 검증 중 -->
  <div v-if="loading.passwordTokenVerification" class="text-gray-500 text-center">
    🔄 링크 검증 중입니다...
  </div>

  <!-- 검증 실패 -->
  <div v-else-if="errors.verificationCode" class="text-red-500 text-center">
    {{ errors.verificationCode }}
  </div>

  <form v-else @submit.prevent="sendResetPassword" class="p-6 space-y-4">
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
      <p class="text-xs text-gray-500 mt-[-0.5rem](-8px) mx-2">영문 대·소문자/숫자/특수문자 중 2가지 이상 조합, 8자~32자</p>
      <FieldError :message="errors.password" />
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
      <FieldError :message="errors.passwordConfirm" />
    </div>

    <!-- 변경하기 버튼 -->
    <BaseButton
      type="submit"
      class="w-full font-medium transition-colors"
    > 변경하기
    </BaseButton>
  </form>
</template>

<script setup>
import { onMounted } from 'vue'
import { usePasswordForm } from '@/features/auth/usePasswordForm.js'

import BaseInput from '@/components/common/BaseInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const {
  form,
  errors,
  loading,
  sendResetPassword,
  validatePasswordField,
  validatePasswordConfirmField,
  verifyPasswordToken,
} = usePasswordForm()

onMounted(verifyPasswordToken)
</script>
