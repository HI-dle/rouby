import { computed, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useTimer } from '@/shared/utils/timerUtils.js'
import { buildErrorCleaner, buildFieldValidator } from '@/shared/utils/formUtils.js'
import { requestEmailVerification, signup, verifyEmailCode } from './authService.js'
import {
  validateEmail,
  validatePassword,
  validatePasswordConfirm,
  validateSignupForm,
} from './validations.js'

export function useSignupForm() {
  const router = useRouter()

  const form = reactive({
    email: '',
    verificationCode: '',
    password: '',
    passwordConfirm: '',
    isEmailVerified: false,
    isVerificationStep: false,
    lastVerifiedEmail: '',
    verificationToken: ''
  })

  const errors = reactive({
    email: '',
    verificationCode: '',
    password: '',
    passwordConfirm: '',
  })

  const loading = reactive({
    emailVerification: false,
    codeVerification: false,
    signup: false,
  })

  const {
    timeLeft,
    isAvailable: canResend,
    start: startTimer,
    stop: stopTimer,
    reset: resetTimer,
    formatted: formatTime,
  } = useTimer({ initialSeconds: 300, interval: 1000 })

  const validateField = buildFieldValidator(errors)
  const validateEmailField = () => validateField(validateEmail, 'email', form.email)
  const validatePasswordField = () => validateField(validatePassword, 'password', form.password)
  const validatePasswordConfirmField = () =>
    validateField(validatePasswordConfirm, 'passwordConfirm', form.password, form.passwordConfirm)

  const clearErrors = buildErrorCleaner(errors, [
    { field: 'email', get: (state) => state.email },
    { field: 'verificationCode', get: (state) => state.verificationCode },
    { field: 'password', get: (state) => state.password },
    { field: 'passwordConfirm', get: (state) => state.passwordConfirm },
  ])

  watch(() => form, clearErrors, { deep: true })

  const isVerificationButtonEnabled = computed(() => {
    const trimmedEmail = (form.email || '').trim()

    if (!trimmedEmail || loading.emailVerification || !canResend.value) {
      return false
    }

    return !(form.isVerificationStep && trimmedEmail
      === form.lastVerifiedEmail);
  })

  const requestVerification = async () => {
    if (!validateEmailField()) {
      return false
    }

    errors.email = ''
    loading.emailVerification = true

    try {
      await requestEmailVerification(form.email)
      form.lastVerifiedEmail = form.email
      form.isVerificationStep = true
      startTimer()
      return true
    } catch (error) {

      if (error.fieldErrors) {
        Object.assign(errors, error.fieldErrors)
      } else {
        errors.email = error.message
      }

      return false
    } finally {
      loading.emailVerification = false
    }
  }

  watch(
    () => form.email,
    (newEmail, oldEmail) => {
      if (newEmail !== oldEmail && form.isVerificationStep) {
        if (newEmail !== form.lastVerifiedEmail) {
          form.isEmailVerified = false
          form.verificationCode = ''
          errors.verificationCode = ''
        }
      }
    }
  )


  const verifyCode = async () => {
    if (form.verificationCode.length !== 6) {
      errors.verificationCode = '6자리 인증번호를 입력해주세요.'
      return false
    }

    loading.codeVerification = true
    try {
      const response= await verifyEmailCode(form.email, form.verificationCode)
      form.verificationToken = response.data.token
      form.isEmailVerified = true
      stopTimer()
      return true
    } catch (error) {
      errors.verificationCode = error.message
      return false
    } finally {
      loading.codeVerification = false
    }
  }

  watch(
    () => form.verificationCode,
    (newCode, oldCode) => {
      if (newCode !== oldCode && errors.verificationCode) {
        errors.verificationCode = ''
      }

      if (newCode.length === 6 && !form.isEmailVerified) {
        void verifyCode()
      }
    }
  )

  const onSubmit = async () => {
    if (!validateSignupForm(form, errors)) {
      return
    }
    if (!form.isEmailVerified) {
      errors.email = '이메일 인증을 완료해주세요.'
      return
    }

    loading.signup = true

    try {
      const res = await signup(form)
      if (res?.data.ok) {
        await router.push('/auth/login')
      }
    } catch (err) {
      if (err.fieldErrors) {
        Object.assign(errors, err.fieldErrors)
      } else {
        errors.email = err.message
      }
    } finally {
      loading.signup = false
    }
  }

  const resendVerification = async () => {
    if (!canResend.value) {
      return
    }
    resetTimer()
    await requestVerification()
  }

  // 콘솔 테스트용
  window.testSignup = {
    // 1. 폼 데이터 자동 입력
    fill: () => {
      form.email = 'test@test.com'
      form.password = 'Test123!'
      form.passwordConfirm = 'Test123!'
      console.log('테스트 데이터 입력 완료')
    },

    // 2. 인증 단계로 이동
    startVerification: () => {
      form.isVerificationStep = true
      startTimer()
      console.log('인증 단계 시작 - 타이머 작동 중')
    },

    // 3. 인증 완료 처리
    completeVerification: () => {
      form.verificationCode = '123456'
      form.isEmailVerified = true
      stopTimer()
      console.log('인증 완료! 가입 버튼 활성화됨')
    },

    // 4. 에러 표시
    showError: (field = 'email', message = '테스트 에러') => {
      errors[field] = message
      console.log(` ${field} 에러:`, message)
    },

    // 5. 모든 상태 리셋
    reset: () => {
      form.email = ''
      form.password = ''
      form.passwordConfirm = ''
      form.verificationCode = ''
      form.isEmailVerified = false
      Object.keys(errors).forEach((key) => delete errors[key])
      stopTimer()
      console.log('모든 상태 초기화')
    },

    // 6. 현재 상태 확인
    status: () => {
      console.log('📊 현재 상태:', {
        email: form.email,
        isVerificationStep: form.isVerificationStep,
        isVerified: form.isEmailVerified,
        timeLeft: timeLeft.value,
        errors: { ...errors },
      })
    },
  }

  return {
    form,
    errors,
    loading,
    timeLeft,
    isResendAvailable: canResend,
    formatTime,
    requestVerification,
    verifyCode,
    onSubmit,
    resendVerification,
    validateEmailField,
    validatePasswordField,
    validatePasswordConfirmField,
    isVerificationButtonEnabled,
  }
}
