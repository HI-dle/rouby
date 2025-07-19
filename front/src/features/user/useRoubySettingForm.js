import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { readRoubySetting, updateRoubySetting } from '@/features/user/userService.js'
import { isValidKeyword } from '@/shared/composable/useKeywordValidator.js'

export function useRoubySettingForm() {
  const communicationTone = ref([])       // 말투 태그 목록
  const keyword = ref('')              // 입력창 값
  const keywordError = ref('')            // 에러 메시지
  const router = useRouter()

  const notifyMorningBriefing = ref(true)
  const notifyBeforeSchedule = ref(false)
  const notifyBeforeRoutine = ref(true)

  const notificationSettings = computed(() => [
    {
      notificationType: 'BRIEFING',
      enabled: notifyMorningBriefing.value
    },
    {
      notificationType: 'SCHEDULE',
      enabled: notifyBeforeSchedule.value
    },
    {
      notificationType: 'ROUTINE',
      enabled: notifyBeforeRoutine.value
    }
  ])

  async function loadInitialSettings() {
    try {
      const res = await readRoubySetting()
      communicationTone.value = Array.isArray(res.data.communicationTone)
        ? res.data.communicationTone
        : []

      if (Array.isArray(res.data.notificationSettings)) {
        res.data.notificationSettings.forEach(item => {
          switch (item.notificationType) {
            case 'BRIEFING':
              notifyMorningBriefing.value = item.enabled
              break
            case 'SCHEDULE':
              notifyBeforeSchedule.value = item.enabled
              break
            case 'ROUTINE':
              notifyBeforeRoutine.value = item.enabled
              break
          }
        })
      } else {
        // 기본값 지정
        notifyMorningBriefing.value = true
        notifyBeforeSchedule.value = false
        notifyBeforeRoutine.value = true
      }
    } catch (err) {
      console.error('초기 설정 로드 실패', err)
    }
  }


  const saveSettings = async () => {
    const payload = {
      communicationTone: communicationTone.value,
      notificationSettings: notificationSettings.value
    }

    try {
      const success = await updateRoubySetting(payload)
      if (success) {
        router.push('/mypage')
      }
    } catch (err) {
      console.error('루비 설정 저장 실패:', err)
      // 에러 처리 추가 가능
    }
  }

  function addToneTag(maxKeywordCount = 3) {
    const raw = keyword.value

    if (communicationTone.value.length >= maxKeywordCount) {
      keywordError.value =  `태그는 최대 ${maxKeywordCount}개까지 입력할 수 있어요.`
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
      if (!communicationTone.value.includes(tag)) {
        communicationTone.value.push(tag)
        added = true
      }
    })

    keywordError.value = added ? '' : '이미 추가된 말투입니다.'
    keyword.value = ''
  }

  function removeToneTag(tag) {
    communicationTone.value = communicationTone.value.filter(t => t !== tag)
  }


  return {
    communicationTone,
    keyword,
    keywordError,
    notifyMorningBriefing,
    notifyBeforeSchedule,
    notifyBeforeRoutine,
    saveSettings,
    addToneTag,
    loadInitialSettings,
    removeToneTag,
  }
}
