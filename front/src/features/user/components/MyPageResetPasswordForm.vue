<template>
  <form @submit.prevent="sendResetPassword" class="p-6 space-y-4">
    <!-- 기존 비밀번호 -->
    <div class="space-y-2">
      <BaseInput
        v-model="form.password"
        label="기존 비밀번호 확인"
        type="password"
        placeholder="비밀번호를 입력하세요"
        :error="errors.password"
        label-class="text-auth-label-color"
        class="placeholder-placeholder-pink-color"
        @blur="validatePasswordField"
      />
      <FieldError :message="errors.password" />
    </div>

    <!-- 새 비밀번호 -->
    <div class="space-y-2">
      <BaseInput
        v-model="form.newPassword"
        label="변경할 비밀번호"
        type="password"
        placeholder="비밀번호를 입력하세요"
        :error="errors.password"
        label-class="text-auth-label-color"
        class="placeholder-placeholder-pink-color"
        @blur="validatePasswordField"
      />
      <p class="text-xs text-gray-500 mt-[-0.5rem] mx-2">
        영문 대·소문자/숫자/특수문자 중 2가지 이상 조합, 8자~32자
      </p>
      <FieldError :message="errors.password" />
    </div>

    <!-- 비밀번호 확인 -->
    <div class="space-y-2">
      <BaseInput
        v-model="form.passwordConfirm"
        label="비밀번호 확인"
        type="password"
        placeholder="비밀번호를 다시 입력하세요"
        :error="errors.passwordConfirm"
        label-class="text-auth-label-color"
        class="placeholder-placeholder-pink-color"
        @blur="validatePasswordConfirmField"
      />
      <FieldError :message="errors.passwordConfirm" />
    </div>

    <!-- API 에러 -->
    <FieldError :message="errors.apiResult" />
    <!-- 성공 메시지 추가 -->
    <p v-if="successMessage.message" class="text-green-600 text-sm font-medium text-center mb-[-0.5rem]">
      {{ successMessage.message }}
    </p>

    <!-- 제출 버튼 -->
    <BaseButton
      type="submit"
      class="w-full font-medium transition-colors"
      :loading="loading.reset"
    >
      변경하기
    </BaseButton>
  </form>
</template>

<script setup>
import { usePasswordForm } from '@/features/user/usePasswordForm.js'
import BaseInput from '@/components/common/BaseInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const {
  form,
  errors,
  loading,
  successMessage,
  validatePasswordField,
  validatePasswordConfirmField,
  sendResetPassword
} = usePasswordForm()
</script>
