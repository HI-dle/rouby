import axios from '@/api/axios.js'

export async function sendResetPasswordLink(email) {
  try {
    const response = await axios.post('/v1/users/password/find', { email })
    if (response.status === 204) {
      return null
    }
    return response.data
  } catch (error) {
    const message =
      error.response?.data?.message || '비밀번호 재설정 링크 전송에 실패했습니다.'
    throw new Error(message)
  }
}

export async function resetPassword({ token, newPassword }) {
  try {
    const response = await axios.patch('/v1/users/password/reset/token', {
      token,
      newPassword
    })
    return response.data ?? null
  } catch (error) {
    // 서버에서 message 필드를 응답한다고 가정
    throw error.response?.data?.message || '비밀번호 변경 요청 중 오류가 발생했습니다.'
  }
}

