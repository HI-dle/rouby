import { ref } from 'vue'

export function useNicknameForm() {
  const nickname = ref('')
  const nicknameError = ref('')
  const isFocused = ref(false)

  const validateNickname = () => {
    if (nickname.value.length > 20) {
      nicknameError.value = '닉네임은 20자 이하여야 합니다.'
    } else {
      nicknameError.value = ''
    }
  }

  return {
    nickname,
    nicknameError,
    isFocused,
    validateNickname,
  }
}
