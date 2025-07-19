import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/features/auth/loginApi.js'
import { storeToken } from '@/features/auth/storeToken.js'
import { getUserBasicInfo } from '@/features/auth/getUserBasicInfo.js'
import { useUserStore } from '@/features/user/store/useUserStore.js'

export function useLoginForm() {
  const router = useRouter()
  const userStore = useUserStore()

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
      const token = await login({
        email: email.value.trim(),
        password: password.value,
      })
      storeToken(token, staySignedIn.value)

      const userInfo = await getUserBasicInfo()
      userStore.setUserInfo(userInfo)

      const redirectPath = userInfo.onboardingStatePath || '/'
      await router.push(redirectPath)
    } catch (e) {
      loginError.value = e.response?.data?.message || '아이디 혹은 비밀번호가 일치하지 않습니다.'
    }
  }

  console.log('유저 스토어 상태 확인:', {
    id: userStore.id,
    email: userStore.email,
    nickname: userStore.nickname,
    healthStatusKeywords: userStore.healthStatusKeywords,
    profileKeywords: userStore.profileKeywords,
    communicationTone: userStore.communicationTone,
  })

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
