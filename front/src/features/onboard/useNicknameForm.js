// useNicknameForm.js 수정 (store 반영)
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
