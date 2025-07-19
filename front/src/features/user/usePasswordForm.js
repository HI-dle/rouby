import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import {
  buildErrorCleaner,
  buildFieldValidator,
} from '@/shared/utils/formUtils.js'
import { myPageResetPassword } from '@/features/user/userService.js'
import {
  validatePassword,
  validatePasswordConfirm,
} from '@/features/auth/validations.js'

export function usePasswordForm() {
  const router = useRouter()

  const form = reactive({
    password: '',
    passwordConfirm: '',
    newPassword: '',
  })

  const errors = reactive({
    password: '',
    passwordConfirm: '',
    apiResult: '',
  })

  const loading = reactive({
    reset: false,
  })

  const successMessage = reactive({
    message: '',
  })

  const validateField = buildFieldValidator(errors)

  const validatePasswordField = () =>
    validateField(validatePassword, 'password', form.password)

  const validatePasswordConfirmField = () =>
    validateField(
      validatePasswordConfirm,
      'passwordConfirm',
      form.newPassword,
      form.passwordConfirm,
    )

  const clearErrors = buildErrorCleaner(errors, [
    { field: 'password', get: (state) => state.password },
    {
      field: 'passwordConfirm',
      get: (state) => state.passwordConfirm,
    },
  ])

  const sendResetPassword = async () => {
    const isPasswordValid = validatePasswordField()
    const isPasswordConfirmValid = validatePasswordConfirmField()

    if (!isPasswordValid || !isPasswordConfirmValid) return

    loading.reset = true
    try {
      const success = await myPageResetPassword(form)
      if (success) {
        successMessage.message = '비밀번호가 성공적으로 변경되었습니다.'
        // await router.push('/auth/login')
      }
    } catch (err) {
      if (err.fieldErrors) {
        Object.assign(errors, err.fieldErrors)
      } else {
        errors.apiResult = '비밀번호 변경에 실패했습니다.'
      }
    } finally {
      loading.reset = false
    }
  }

  return {
    form,
    errors,
    loading,
    successMessage,
    validatePasswordField,
    validatePasswordConfirmField,
    sendResetPassword,
  }
}
