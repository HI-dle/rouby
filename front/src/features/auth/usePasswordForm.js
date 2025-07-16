// usePasswordForm.js
import { reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  findPassword,
  resetPassword,
  verificationPasswordCode
} from '@/features/auth/authService'
import {
  validatePassword,
  validatePasswordConfirm,
  validateEmail
} from '@/features/auth/validations.js'
import { buildFieldValidator } from '@/utils/formUtils.js'

export function usePasswordForm() {
  const route = useRoute()
  const router = useRouter()

  const form = reactive({
    email: '',
    token: '',
    password: '',
    passwordConfirm: '',
    isVerificationStep: false, // 추가: 이메일 입력 단계 컨트롤용
  })

  const errors = reactive({
    email: '',
    password: '',
    passwordConfirm: '',
    verificationCode: '',
  })

  const loading = reactive({
    passwordTokenVerification: false,
  })

  const validateField = buildFieldValidator(errors)

  const validatePasswordField = () =>
    validateField(validatePassword, 'password', form.password)

  const validatePasswordConfirmField = () =>
    validateField(validatePasswordConfirm, 'passwordConfirm', form.password, form.passwordConfirm)

  const validateEmailField = () =>
    validateField(validateEmail, 'email', form.email)

  const sendResetPasswordLink = async () => {
    validateEmailField()
    if (errors.email) return

    try {
      const success = await findPassword(form.email)
      if (success) {
        form.isVerificationStep = true
        await router.push('/login') // 혹은 다른 경로
      }
    } catch (err) {
      if (err.fieldErrors) {
        Object.assign(errors, err.fieldErrors)
      } else {
        errors.email = err.message
      }
    }
  }

  const sendResetPassword = async () => {
    validatePasswordField()
    validatePasswordConfirmField()
    if (errors.password || errors.passwordConfirm) return

    try {
      const success = await resetPassword(form)
      if (success) {
        await router.push('/login')
      }
    } catch (err) {
      if (err.fieldErrors) {
        Object.assign(errors, err.fieldErrors)
      } else {
        errors.password = err.message
      }
    }
  }

  const verifyPasswordToken = async () => {
    form.email = route.query.email || ''
    form.token = route.query.token || ''

    if (!form.email || !form.token) {
      errors.verificationCode = '잘못된 접근입니다.'
      return
    }

    loading.passwordTokenVerification = true
    try {
      const success = await verificationPasswordCode(form)
      if (!success) {
        errors.verificationCode = '링크가 만료되었거나 유효하지 않습니다.'
      }
    } catch (err) {
      if (err.fieldErrors) {
        Object.assign(errors, err.fieldErrors)
      } else {
        errors.verificationCode = err.message
      }
    } finally {
      loading.passwordTokenVerification = false
    }
  }

  return {
    form,
    errors,
    loading,
    validateEmailField,
    validatePasswordField,
    validatePasswordConfirmField,
    sendResetPasswordLink,
    sendResetPassword,
    verifyPasswordToken,
  }
}
