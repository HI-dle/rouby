
import { ref } from 'vue'
import { isValidKeyword } from '@/components/composable/useKeywordValidator.js'

export function useKeywordForm() {
  const keyword = ref('')
  const keywordError = ref('')
  const keywords = ref([])

  const addKeyword = () => {
    const input = keyword.value.trim()
    const { valid, message } = isValidKeyword(input)

    if (!valid) {
      keywordError.value = message
      return
    }

    if (keywords.value.includes(input)) {
      keywordError.value = '이미 추가된 태그입니다.'
      return
    }

    keywords.value.push(input)
    keyword.value = ''
    keywordError.value = ''
  }

  const removeKeyword = (target) => {
    keywords.value = keywords.value.filter((k) => k !== target)
  }

  const handleSubmit = () => {
    addKeyword()
  }

  return {
    keyword,
    keywordError,
    keywords,
    handleSubmit,
    removeKeyword,
  }
}
