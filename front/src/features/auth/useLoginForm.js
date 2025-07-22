// src/features/auth/useLoginForm.js
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/features/auth/loginApi.js'
import { storeToken } from '@/features/auth/storeToken.js'
import axios from '@/api/axios.js'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

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
    emailError.value = ''
    passwordError.value = ''
    loginError.value = ''

    validateEmail()
    validatePassword()

    if (emailError.value || passwordError.value) return

    try {
      // 1. 로그인 요청
      const token = await login({
        email: email.value.trim(),
        password: password.value,
      })

      // 2. 토큰 저장
      storeToken(token, staySignedIn.value)
      console.log('[토큰 저장 완료]', token)

      // 3. 유저 기본 정보 요청
      const userRes = await axios.get('/v1/users/basic-info')
      const user = userRes.data
      console.log('[유저 기본 정보]', user)

      // 4. Pinia 저장소에 값 저장
      const onboardStore = useOnboardStore()
      onboardStore.userName = user.nickname || ''
      onboardStore.selectedHealth = [...(user.healthStatusKeywords || [])]
      onboardStore.personalKeyword = [...(user.profileKeywords || [])]
      onboardStore.speechType = [...(user.communicationTone || [])]

      // 5. 라우팅
      const onboardingStatePath = user.onboardingStatePath
      if (!onboardingStatePath) {
        throw new Error('onboardingStatePath가 없습니다.')
      }

      await router.push(onboardingStatePath)

    } catch (e) {
      console.error('[로그인 또는 유저 정보 조회 실패]', e)

      if (e.response?.status === 401) {
        loginError.value = e.response.data.message || '아이디 혹은 비밀번호가 일치하지 않습니다.'
      }
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
