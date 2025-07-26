import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {readUserInfo, updateUserInfo} from '@/features/user/userService.js'
import {isValidKeyword} from '@/shared/composable/useKeywordValidator.js'

export function useUserInfoForm() {
  const healthStatusKeywords = ref([])       // 말투 태그 목록
  const profileKeywords = ref([])       // 말투 태그 목록
  const healthStatusKeyword = ref('')              // 입력창 값
  const profileKeyword = ref('')              // 입력창 값
  const keywordError = ref('')            // 에러 메시지
  const router = useRouter()

  const nickname = ref('')
  const dailyStartTime = ref(null)
  const dailyEndTime = ref(null)

  async function loadInitialSettings() {
    try {
      const res = await readUserInfo()
      healthStatusKeywords.value = Array.isArray(res.data.healthStatusKeywords)
        ? res.data.healthStatusKeywords : []
      profileKeywords.value = Array.isArray(res.data.profileKeywords)
        ? res.data.profileKeywords : []

      nickname.value = res.data.nickname
      dailyStartTime.value = res.data.dailyStartTime
      dailyEndTime.value = res.data.dailyEndTime

    } catch (err) {
      console.error('초기 설정 로드 실패', err)
    }
  }


  const saveSettings = async () => {
    const payload = {
      nickname: nickname.value,
      healthStatusKeywords: healthStatusKeywords.value,
      profileKeywords: profileKeywords.value,
      dailyStartTime: dailyStartTime.value,
      dailyEndTime: dailyEndTime.value,
    }

    try {
      const success = await updateUserInfo(payload)
      if (success) {
        router.push('/mypage')
      }
    } catch (err) {
      console.error('내 정보 수정 실패:', err)
      // 에러 처리 추가 가능
    }
  }

  function addProfileKeywordsTag(profileKeywordsCount = 3) {
    const raw = profileKeyword.value

    if (profileKeywords.value.length >= profileKeywordsCount) {
      keywordError.value =  `태그는 최대 ${profileKeywordsCount}개까지 입력할 수 있어요.`
      return
    }

    if (!raw || typeof raw !== 'string' || raw.trim() === '') {
      keywordError.value = '입력값이 비어 있습니다.'
      return
    }

    const { valid, message } = isValidKeyword(raw)
    if (!valid) {
      keywordError.value = message
      return
    }

    const tags = raw
    .split(',')
    .map(tag => tag.trim())
    .filter(tag => tag.length > 0)

    if (tags.length === 0) {
      keywordError.value = '유효한 말투를 입력해주세요.'
      return
    }

    let added = false

    tags.forEach(tag => {
      if (!profileKeywords.value.includes(tag)) {
        profileKeywords.value.push(tag)
        added = true
      }
    })

    keywordError.value = added ? '' : '이미 추가된 말투입니다.'
    profileKeyword.value = ''
  }

  function removeProfileKeywordsTag(tag) {
    profileKeywords.value = profileKeywords.value.filter(t => t !== tag)
  }

  function addHealthStatusKeywordsTag(healthStatusKeywordsCount = 3) {
    const raw = healthStatusKeyword.value

    if (healthStatusKeywords.value.length >= healthStatusKeywordsCount) {
      keywordError.value =  `태그는 최대 ${healthStatusKeywordsCount}개까지 입력할 수 있어요.`
      return
    }

    if (!raw || typeof raw !== 'string' || raw.trim() === '') {
      keywordError.value = '입력값이 비어 있습니다.'
      return
    }

    const { valid, message } = isValidKeyword(raw)
    if (!valid) {
      keywordError.value = message
      return
    }

    const tags = raw
      .split(',')
      .map(tag => tag.trim())
      .filter(tag => tag.length > 0)

    if (tags.length === 0) {
      keywordError.value = '유효한 말투를 입력해주세요.'
      return
    }

    let added = false

    tags.forEach(tag => {
      if (!healthStatusKeywords.value.includes(tag)) {
        healthStatusKeywords.value.push(tag)
        added = true
      }
    })

    keywordError.value = added ? '' : '이미 추가된 말투입니다.'
    healthStatusKeyword.value = ''
  }

  function removeHealthStatusKeywordsTag(tag) {
    healthStatusKeywords.value = healthStatusKeywords.value.filter(t => t !== tag)
  }


  return {
    healthStatusKeywords,
    profileKeywords,
    keywordError,
    healthStatusKeyword,
    profileKeyword,
    nickname,
    dailyStartTime,
    dailyEndTime,
    loadInitialSettings,
    saveSettings,
    addHealthStatusKeywordsTag,
    removeHealthStatusKeywordsTag,
    addProfileKeywordsTag,
    removeProfileKeywordsTag
  }
}
