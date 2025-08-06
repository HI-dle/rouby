import { ref } from 'vue'

export function useNicknameForm(initialValue = '') {
  const nickname = ref(initialValue)
  const nicknameError = ref('')
  const isFocused = ref(false)

  const validateNickname = () => {
    if (!nickname.value.trim()) {
      nicknameError.value = '닉네임을 입력해주세요.'
      return false
    }
    if (nickname.value.trim().length > 20) {
      nicknameError.value = '닉네임은 20자 이하여야 합니다.'
      return false
    }
    nicknameError.value = ''
    return true
  }

  return {
    nickname,
    nicknameError,
    isFocused,
    validateNickname,
  }
}
