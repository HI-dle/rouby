<script setup>
  import { ref, watch, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import PasswordKeyIcon from '@/assets/password_key.svg'
  import BaseInput from '@/components/common/BaseInput.vue'
  import { isValidPassword, validateResetToken } from '@/features/user/validations.js'
  import { resetPassword } from '@/features/user/password.js'

  const currentPassword = ref('')
  const password = ref('')
  const confirmPassword = ref('')
  const passwordError = ref('')
  const isLoading = ref(false)
  const router = useRouter()
  const route = useRoute()
  const userId = ref(null)
  const error = ref(null)

  const props = defineProps({
    token: {
      type: String,
      required: true,
    },
  })

  onMounted(async () => {
    const { userId: id, error: err } = await validateResetToken(props.token)

    if (err) {
      error.value = err
      router.push('/login')
    } else {
      userId.value = id
    }

    isLoading.value = false
  })

  watch(password, (value) => {
    if (!value) {
      passwordError.value = ''
    } else if (!isValidPassword(value)) {
      passwordError.value = '영문/숫자/특수문자 중 2가지 이상 조합, 8~32자여야 합니다.'
    } else {
      passwordError.value = ''
    }
  })

  async function handleSubmit() {
    if (!password.value) {
      alert('비밀번호를 입력해주세요.')
      return
    }

    if (passwordError.value) {
      alert(passwordError.value)
      return
    }

    if (password.value !== confirmPassword.value) {
      alert('비밀번호 확인이 일치하지 않습니다.')
      return
    }

    isLoading.value = true

    try {
      const token = route.query.token
      await resetPassword({ token, newPassword: password.value })
      alert('비밀번호가 성공적으로 변경되었습니다.')
      router.push('/login')
    } catch (error) {
      console.error('Error:', error)
      alert('전송 중 오류가 발생했습니다. 다시 시도해주세요.')
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
              <img :src="PasswordKeyIcon" alt="패스워드 아이콘" />
            </figure>
          </div>
        </div>

        <!-- Title -->
        <h1 class="title text-main-color">비밀번호 변경</h1>

        <!-- Subtitle -->
        <p class="subtitle">비밀번호를 재설정 해주세요</p>

        <!-- Password BaseInput -->
        <BaseInput
          label="기존 비밀번호"
          v-model="currentPassword"
          placeholder="••••••••"
          type="password"
        />
        <BaseInput
          label="변경할 비밀번호"
          v-model="password"
          placeholder="••••••••"
          type="password"
          :error-message="passwordError"
        />
        <BaseInput
          label="비밀번호 확인"
          v-model="confirmPassword"
          placeholder="••••••••"
          type="password"
        />

        <!-- Submit Button -->
        <button @click="handleSubmit" class="submit-button">
          {{ '비밀번호 수정하기' }}
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
