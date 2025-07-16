import { ref, watch } from 'vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

export function useNicknameForm() {
  const store = useOnboardStore()

  const nickname = ref(store.userName)  // store에서 초기값 가져오기
  const nicknameError = ref('')
  const isFocused = ref(false)

  // 닉네임이 바뀌면 store에 반영
  watch(nickname, (val) => {
    store.userName = val
  })

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
