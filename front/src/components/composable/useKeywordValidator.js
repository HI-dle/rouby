export const MAX_KEYWORD_LENGTH = 20

export function isValidKeyword(keyword) {
  const trimmed = keyword.trim()
  if (!trimmed) return { valid: false, message: '' }
  if (trimmed.length > MAX_KEYWORD_LENGTH)
    return { valid: false, message: `태그는 ${MAX_KEYWORD_LENGTH}자 이하로 입력해주세요.` }

  return { valid: true, message: '' }
}
