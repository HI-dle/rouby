// src/features/auth/useLoginForm.js
import { ref } from 'vue'
import { useGoBack } from '@/shared/composable/useGoBack'
import { loginAndBootstrap } from './authService'

export function useLoginForm() {
  const { goPathOrBack } = useGoBack()

  const email = ref('')
  const password = ref('')
  const staySignedIn = ref(false)

  const emailError = ref('')
  const passwordError = ref('')
  const loginError = ref('')

  const showForceLoginModal = ref(false)
  let pendingForceLogin = null

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

  async function handleForceLogin() {
    if (!pendingForceLogin) return
    showForceLoginModal.value = false
    await onLogin(true) // 강제 로그인
    pendingForceLogin = null
  }

  async function onLogin(isForceLogin = false) {
    emailError.value = ''
    passwordError.value = ''
    loginError.value = ''

    validateEmail()
    validatePassword()

    if (emailError.value || passwordError.value) return

    try {
      const nextPath = await loginAndBootstrap(
        email.value,
        password.value,
        staySignedIn.value,
        isForceLogin,
      )

      await goPathOrBack(nextPath || '/')
    } catch (e) {
      console.error('[로그인 또는 유저 정보 조회 실패]', e)

      if (e.response?.data?.code === 'TOO_MANY_SESSIONS') {
        // 세션 초과 → 모달 열기
        pendingForceLogin = { email: email.value, password: password.value, staySignedIn: staySignedIn.value }
        showForceLoginModal.value = true
      } else if (e.response?.status === 401) {
        loginError.value = e.response.data.message || '아이디 혹은 비밀번호가 일치하지 않습니다.'
      }
    }
  }

  const onGoogleLogin = () => {
    alert('서비스를 준비 중입니다.')
  }

  const onNaverLogin = () => {
    alert('서비스를 준비 중입니다.')
  }

  const onAppleLogin = () => {
    alert('서비스를 준비 중입니다.')
  }

  return {
    email,
    password,
    staySignedIn,
    emailError,
    passwordError,
    loginError,
    showForceLoginModal,
    validateEmail,
    validatePassword,
    onLogin,
    handleForceLogin,
    onGoogleLogin,
    onNaverLogin,
    onAppleLogin,
  }
}
