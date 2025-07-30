import { ref } from 'vue'
import { isValidKeyword } from '@/shared/composable/useKeywordValidator.js'

export function useKeywordForm(initialKeywords = [], maxKeywordCount = 10) {
  const keyword = ref('')
  const keywords = ref([...initialKeywords])
  const keywordError = ref('')

  const handleSubmit = () => {
    const value = keyword.value.trim()

    // 최대 개수 제한
    if (keywords.value.length >= maxKeywordCount) {
      keywordError.value = `태그는 최대 ${maxKeywordCount}개까지 입력할 수 있어요.`
      return
    }

    // 유효성 검사
    const { valid, message } = isValidKeyword(value)
    if (!valid) {
      keywordError.value = message
      return
    }

    // 중복 방지
    if (keywords.value.includes(value)) {
      keywordError.value = '이미 추가된 키워드예요.'
      return
    }

    // 정상 추가
    keywords.value.push(value)
    keyword.value = ''
    keywordError.value = ''
  }

  const removeKeyword = (k) => {
    keywords.value = keywords.value.filter((v) => v !== k)
  }

  return {
    keyword,
    keywords,
    keywordError,
    handleSubmit,
    removeKeyword,
  }
}
