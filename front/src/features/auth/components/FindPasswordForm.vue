<template>
  <form @submit.prevent="sendResetPasswordLink" class="p-6 space-y-4">
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
      </div>
      <FieldError :message="errors.email" />
    </div>

    <!-- 재설정 링크 전송 버튼 -->
    <BaseButton
      type="submit"
      class="w-full font-medium transition-colors"
    >
     재설정 링크 전송
    </BaseButton>
  </form>
</template>

<script setup>
import { useSignupForm } from '../useSignupForm.js'
import BaseInput from '@/components/common/BaseInput.vue'
import FieldError from '@/components/common/FieldError.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const {
  form,
  errors,
  sendResetPasswordLink,
  validateEmailField,
} = useSignupForm()
</script>
