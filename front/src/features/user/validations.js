import axios from '@/api/axios.js'

export function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

export function isValidPassword(password) {
  if (password.length < 8 || password.length > 32) return false

  const hasLetter = /[a-zA-Z]/.test(password)
  const hasNumber = /[0-9]/.test(password)
  const hasSpecial = /[^a-zA-Z0-9]/.test(password)

  const validTypes = [hasLetter, hasNumber, hasSpecial].filter(Boolean).length
  return validTypes >= 2
}

export async function validateResetToken(token) {
  try {
    const response = await axios.get(`/v1/users/password/reset/token?token=${token}`)
    return { userId: response.data.userId, error: null }
  } catch (error) {
    const errorMessage = '유효하지 않거나 만료된 링크입니다.'
    console.error(error)
    return { userId: null, error: errorMessage }
  }
}
