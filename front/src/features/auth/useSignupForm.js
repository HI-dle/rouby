import { reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useTimer } from '@/utils/timerUtils.js'
import { buildFieldValidator, buildErrorCleaner } from '@/utils/formUtils.js'

import { requestEmailVerification, verifyEmailCode, signup } from './authService.js'

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

  const hasRequestedVerification = ref(false)

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
    { field: 'email',            get: state => state.email },
    { field: 'verificationCode', get: state => state.verificationCode },
    { field: 'password',         get: state => state.password },
    { field: 'passwordConfirm',  get: state => state.passwordConfirm },
  ])


  watch(() => form, clearErrors, { deep: true })

  const requestVerification = async () => {
    if (!validateEmailField()) {
      return false
    }

    loading.emailVerification = true
    try {
      await requestEmailVerification(form.email)
      hasRequestedVerification.value = true
      form.isVerificationStep = true
      startTimer()
      return true
    } finally {
      loading.emailVerification = false
    }
  }

  const verifyCode = async () => {
    if (form.verificationCode.length !== 6) {
      errors.verificationCode = '6자리 인증번호를 입력해주세요.'
      return false
    }

    loading.codeVerification = true
    try {
      await verifyEmailCode(form.email, form.verificationCode)
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
    (newCode) => {
      if (newCode.length === 6 && !form.isEmailVerified) {
        void verifyCode()
      }
    },
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
      const success = await signup(form)
      if (success) {
        await router.push('/login')
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

  return {
    form,
    errors,
    loading,
    hasRequestedVerification,
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
  }
}
