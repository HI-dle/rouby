<script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import PasswordLockIcon from '@/assets/password_lock.svg'
  import BaseInput from '@/components/common/BaseInput.vue'
  import { sendResetPasswordLink } from '@/features/user/password.js'
  import { isValidEmail } from '@/features/user/validations.js'

  const email = ref('')
  const isLoading = ref(false)
  const message = ref('')
  const messageType = ref('')
  const isSubmitted = ref(false)
  const router = useRouter()

  async function handleSubmit() {
    isSubmitted.value = true
    message.value = ''
    messageType.value = ''

    if (!isValidEmail(email.value)) {
      message.value = '올바른 이메일 주소를 입력해주세요.'
      messageType.value = 'error'
      return
    }

    isLoading.value = true

    try {
      await sendResetPasswordLink(email.value)

      message.value = '이메일 링크를 전송하였습니다.'
      messageType.value = 'success'

    } catch (error) {
      console.error('Error:', error)
      message.value = '전송 중 오류가 발생했습니다. 다시 시도해주세요.'
      messageType.value = 'error'
    } finally {
      isLoading.value = false
    }
  }

  function goBack() {
    router.push('/login')
  }
</script>

<template>
  <div class="main-container">
    <div class="password-reset-container">
      <!-- Main Content -->
      <div class="main-content">
        <!-- Lock Icon -->
        <div class="lock-container">
          <div class="lock-icon">
            <figure>
              <img :src="PasswordLockIcon" alt="패스워드 아이콘" />
            </figure>
          </div>
        </div>

        <!-- Title -->
        <h1 class="title text-main-color">비밀번호 찾기</h1>

        <!-- Subtitle -->
        <p class="subtitle">
          가입하신 이메일 정보를 입력해주시면<br />
          비밀번호 재설정 링크를 보내드려요
        </p>

        <!-- Email BaseInput -->
        <BaseInput
          label="이메일 주소"
          v-model="email"
          placeholder="user@email.com"
          @focus="message = ''"
          :error-message="isSubmitted && messageType === 'error' ? message : ''"
        />

        <p
          v-if="messageType === 'success'"
          class="text-main-color"
        >
          {{ message }}
        </p>

        <!-- Submit Button -->
        <button @click="handleSubmit" class="submit-button" :disabled="!email || isLoading">
          {{ isLoading ? '전송중...' : '재설정 링크 전송' }}
        </button>

        <!-- Back Link -->
        <button @click="goBack" class="back-link">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M15 18l-6-6 6-6"></path>
          </svg>
          로그인 페이지로 돌아가기
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.password-reset-container {
  max-width: 375px;
  margin: 0 auto;
  background: white;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  position: relative;
}

/* Main Content */
.main-content {
  padding: 32px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.lock-container {
  margin-bottom: 32px;
}

.lock-icon {
  width: 64px;
  height: 64px;
  background: rgba(99, 102, 241, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6366f1;
}

.lock-icon svg {
  width: 32px;
  height: 32px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 12px;
  text-align: center;
}

.subtitle {
  color: #6b7280;
  text-align: center;
  line-height: 1.6;
  margin-bottom: 40px;
  font-size: 14px;
}

.submit-button {
  width: 100%;
  padding: 18px;
  background: #6366f1;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: 10px;
  margin-bottom: 24px;
}

.submit-button:hover:not(:disabled) {
  background: #5856eb;
  transform: translateY(-1px);
}

.submit-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.back-link {
  display: flex;
  align-items: center;
  color: #6366f1;
  font-size: 14px;
  font-weight: 500;
  background: none;
  border: none;
  cursor: pointer;
  transition: color 0.2s ease;
}

.back-link:hover {
  color: #5856eb;
}

.back-link svg {
  width: 16px;
  height: 16px;
  margin-right: 4px;
}

/* 반응형 디자인 */
@media (max-width: 375px) {
  .password-reset-container {
    max-width: 100%;
  }

  .main-content {
    padding: 24px 20px;
  }

  .title {
    font-size: 24px;
  }
}
</style>
