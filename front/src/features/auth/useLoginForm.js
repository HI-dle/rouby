// src/features/auth/useLoginForm.js
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/features/auth/loginApi.js'
import { storeToken } from '@/features/auth/storeToken.js'

export function useLoginForm() {
  const router = useRouter()

  const email = ref('')
  const password = ref('')
  const staySignedIn = ref(false)

  const emailError = ref('')
  const passwordError = ref('')
  const loginError = ref('')

  function validateEmailFormat(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
  }

  function validateEmail() {
    if (!validateEmailFormat(email.value)) {
      emailError.value = '잘못된 이메일 형식입니다.'
    } else {
      emailError.value = ''
    }
  }

  function validatePassword() {
    if (!password.value) {
      passwordError.value = '비밀번호를 입력해주세요.'
    } else {
      passwordError.value = ''
    }
  }

  async function onLogin() {
    // 초기화
    emailError.value = ''
    passwordError.value = ''
    loginError.value = ''

    // 유효성 검사
    validateEmail()
    validatePassword()

    if (emailError.value || passwordError.value) return

    try {
      const token = await login({
        email: email.value.trim(),
        password: password.value,
      })
      storeToken(token, staySignedIn.value)
      await router.push('/')
    } catch (e) {
      loginError.value = e.response?.data?.message || '아이디 혹은 비밀번호가 일치하지 않습니다.'
    }
  }

  return {
    email,
    password,
    staySignedIn,
    emailError,
    passwordError,
    loginError,
    validateEmail,
    validatePassword,
    onLogin,
  }
}
