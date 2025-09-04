import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getPiniaStorage } from '@/shared/utils/piniaPersistUtils'

export const useUserInfoStore = defineStore(
  'userInfo',
  () => {
    const id = ref(null)
    const email = ref('')
    const nickname = ref('')
    const healthStatusKeywords = ref([])
    const profileKeywords = ref([])
    const communicationTone = ref([])
    const startOfDayTime = ref('')
    const endOfDayTime = ref('')
    const scheduleNotiEnabled = ref(false)
    const routineNotiEnabled = ref(false)
    const briefingNotiEnabled = ref(false)

    const setUserInfo = (newInfo) => {
      id.value = newInfo.id ?? id.value
      email.value = newInfo.email ?? email.value
      nickname.value = newInfo.nickname ?? nickname.value
      healthStatusKeywords.value = newInfo.healthStatusKeywords ?? []
      profileKeywords.value = newInfo.profileKeywords ?? []
      communicationTone.value = newInfo.communicationTone ?? []
      startOfDayTime.value = newInfo.startOfDayTime ?? ''
      endOfDayTime.value = newInfo.endOfDayTime ?? ''
      scheduleNotiEnabled.value = newInfo.scheduleNotiEnabled ?? false
      routineNotiEnabled.value = newInfo.routineNotiEnabled ?? false
      briefingNotiEnabled.value = newInfo.briefingNotiEnabled ?? false
    }

    const setUserInfoWithDefaults = (newInfo) => {
      id.value = newInfo.id !== undefined ? newInfo.id : id.value
      email.value = newInfo.email !== undefined ? newInfo.email : email.value
      nickname.value =
        newInfo.nickname !== undefined ? newInfo.nickname : nickname.value

      healthStatusKeywords.value =
        newInfo.healthStatusKeywords !== undefined
          ? newInfo.healthStatusKeywords
          : healthStatusKeywords.value

      profileKeywords.value =
        newInfo.profileKeywords !== undefined
          ? newInfo.profileKeywords
          : profileKeywords.value

      communicationTone.value =
        newInfo.communicationTone !== undefined
          ? newInfo.communicationTone
          : communicationTone.value

      startOfDayTime.value =
        newInfo.startOfDayTime !== undefined
          ? newInfo.startOfDayTime
          : startOfDayTime.value

      endOfDayTime.value =
        newInfo.endOfDayTime !== undefined
          ? newInfo.endOfDayTime
          : endOfDayTime.value

      scheduleNotiEnabled.value =
        newInfo.scheduleNotiEnabled !== undefined
          ? newInfo.scheduleNotiEnabled
          : scheduleNotiEnabled.value

      routineNotiEnabled.value =
        newInfo.routineNotiEnabled !== undefined
          ? newInfo.routineNotiEnabled
          : routineNotiEnabled.value

      briefingNotiEnabled.value =
        newInfo.briefingNotiEnabled !== undefined
          ? newInfo.briefingNotiEnabled
          : briefingNotiEnabled.value
    }

    const reset = () => {
      id.value = null
      email.value = ''
      nickname.value = ''
      healthStatusKeywords.value = []
      profileKeywords.value = []
      communicationTone.value = []
      startOfDayTime.value = ''
      endOfDayTime.value = ''
      scheduleNotiEnabled.value = false
      routineNotiEnabled.value = false
      briefingNotiEnabled.value = false
    }

    return {
      id,
      email,
      nickname,
      healthStatusKeywords,
      profileKeywords,
      communicationTone,
      startOfDayTime,
      endOfDayTime,
      scheduleNotiEnabled,
      routineNotiEnabled,
      briefingNotiEnabled,
      setUserInfo,
      setUserInfoWithDefaults,
      reset,
    }
  },
  {
    persist: {
      key: 'userInfo',
      storage: getPiniaStorage(),
    },
  },
)
