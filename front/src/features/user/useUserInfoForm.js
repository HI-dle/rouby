import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {readUserInfo, updateUserInfo} from '@/features/user/userService.js'
import {isValidKeyword} from '@/shared/composable/useKeywordValidator.js'

export function useUserInfoForm() {
  const router = useRouter()

  // 공통 필드 정의
  const tagFields = {
    health: {
      tags: ref([]),
      input: ref(''),
      error: ref(''),
      maxCount: 3,
    },
    profile: {
      tags: ref([]),
      input: ref(''),
      error: ref(''),
      maxCount: 3,
    },
  }

  const nickname = ref('')
  const dailyStartTime = ref(null)
  const dailyEndTime = ref(null)

  async function loadInitialSettings() {
    try {
      const res = await readUserInfo()
      tagFields.health.tags.value = Array.isArray(res.data.healthStatusKeywords)
        ? res.data.healthStatusKeywords : []
      tagFields.profile.tags.value = Array.isArray(res.data.profileKeywords)
        ? res.data.profileKeywords : []

      nickname.value = res.data.nickname
      dailyStartTime.value = res.data.dailyStartTime
      dailyEndTime.value = res.data.dailyEndTime
    } catch (err) {
      console.error('초기 설정 로드 실패', err)
    }
  }

  async function saveSettings() {
    const payload = {
      nickname: nickname.value,
      healthStatusKeywords: tagFields.health.tags.value,
      profileKeywords: tagFields.profile.tags.value,
      dailyStartTime: dailyStartTime.value,
      dailyEndTime: dailyEndTime.value,
    }

    try {
      const success = await updateUserInfo(payload)
      if (success) router.push('/mypage')
    } catch (err) {
      console.error('내 정보 수정 실패:', err)
    }
  }

  function addTag(fieldKey) {
    const field = tagFields[fieldKey]
    const raw = field.input.value

    if (field.tags.value.length >= field.maxCount) {
      field.error.value = `태그는 최대 ${field.maxCount}개까지 입력할 수 있어요.`
      return
    }

    if (!raw || typeof raw !== 'string' || raw.trim() === '') {
      field.error.value = '입력값이 비어 있습니다.'
      return
    }

    const { valid, message } = isValidKeyword(raw)
    if (!valid) {
      field.error.value = message
      return
    }

    const tags = raw
      .split(',')
      .map(tag => tag.trim())
      .filter(tag => tag.length > 0)

    if (tags.length === 0) {
      field.error.value = '유효한 키워드를 입력해주세요.'
      return
    }

    let added = false
    tags.forEach(tag => {
      if (!field.tags.value.includes(tag)) {
        field.tags.value.push(tag)
        added = true
      }
    })

    field.error.value = added ? '' : '이미 추가된 키워드입니다.'
    field.input.value = ''
  }

  function removeTag(fieldKey, tag) {
    const field = tagFields[fieldKey]
    field.tags.value = field.tags.value.filter(t => t !== tag)
  }

  return {
    nickname,
    dailyStartTime,
    dailyEndTime,
    loadInitialSettings,
    saveSettings,
    // health 상태용
    healthStatusKeywords: tagFields.health.tags,
    healthStatusKeyword: tagFields.health.input,
    healthStatusKeywordError: tagFields.health.error,
    addHealthStatusKeywordsTag: () => addTag('health'),
    removeHealthStatusKeywordsTag: (tag) => removeTag('health', tag),
    // 관심 태그용
    profileKeywords: tagFields.profile.tags,
    profileKeyword: tagFields.profile.input,
    profileKeywordError: tagFields.profile.error,
    addProfileKeywordsTag: () => addTag('profile'),
    removeProfileKeywordsTag: (tag) => removeTag('profile', tag),
  }
}
