// src/features/auth/useLoginForm.js
import { ref } from 'vue'
import { login, getUserBasicInfo } from '@/features/auth/api.js'
import { useGoBack } from '@/shared/composable/useGoBack'
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { useAuthStore } from '@/stores/useAuthStore'

export function useLoginForm() {
  const goPathOrBack = useGoBack()
  const userInfoStore = useUserInfoStore()
  const { setToken, setStaySignedIn } = useAuthStore()

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
    emailError.value = ''
    passwordError.value = ''
    loginError.value = ''

    validateEmail()
    validatePassword()

    if (emailError.value || passwordError.value) return

    try {
      // 1. 로그인 요청
      const response = await login({
        email: email.value.trim(),
        password: password.value,
      })

      // 2. 토큰 저장
      setPiniaStorage(staySignedIn.value)
      setToken(response.data.token)
      setStaySignedIn(staySignedIn.value)

      // 3. 유저 기본 정보 요청
      const userRes = await getUserBasicInfo()
      const user = userRes.data

      userInfoStore.setUserInfoWithDefaults({ ...user })

      // 4. 유저 디바이스 토큰 및 정보 저장

      // 5. 라우팅
      const onboardingStatePath = user.onboardingStatePath
      if (!onboardingStatePath) {
        throw new Error('onboardingStatePath가 없습니다.')
      }

      await goPathOrBack(onboardingStatePath)
    } catch (e) {
      console.error('[로그인 또는 유저 정보 조회 실패]', e)

      if (e.response?.status === 401) {
        loginError.value =
          e.response.data.message || '아이디 혹은 비밀번호가 일치하지 않습니다.'
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
    validateEmail,
    validatePassword,
    onLogin,
    onGoogleLogin,
    onNaverLogin,
    onAppleLogin,
  }
}
