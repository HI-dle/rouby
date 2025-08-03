import axios from '@/api/axios'
import { useUserInfoStore } from '@/stores/useUserInfoStore'

function formatTimeToLocalTimeString(timeStr) {
  // timeStr이 예: "08:00" 또는 "8:00"이라면 "08:00:00"으로 변환
  if (!timeStr) return null
  const parts = timeStr.split(':')
  if (parts.length === 2) {
    // HH:mm -> HH:mm:ss
    return `${parts[0].padStart(2, '0')}:${parts[1].padStart(2, '0')}:00`
  }
  return timeStr // 이미 HH:mm:ss 형태면 그대로
}
const store = useUserInfoStore()

export async function updateUserInfo() {
  const payload = {
    nickname: store.nickname,
    healthStatusKeywords: Array.isArray(store.healthStatusKeywords)
      ? store.healthStatusKeywords
      : [],
    profileKeywords: Array.isArray(store.profileKeywords)
      ? store.profileKeywords
      : [],
    dailyStartTime: formatTimeToLocalTimeString(store.startOfDayTime),
    dailyEndTime: formatTimeToLocalTimeString(store.endOfDayTime),
  }

  try {
    const res = await axios.patch('/v1/users/user-info', payload)
  } catch (error) {
    console.error('유저 정보 업데이트 실패:', error.response ?? error)
    throw error
  }
}

export async function completeUserSetting() {
  try {
    await axios.patch('/v1/users/onboarding/user-info/complete')
  } catch (error) {
    console.error('유저 온보딩 완료 실패:', error)
    throw error
  }
}

export async function updateRoubySetting() {
  const payload = {
    communicationTone: store.communicationTone,
    notificationSettings: [
      {
        notificationType: 'SCHEDULE',
        enabled: store.scheduleNotiEnabled,
      },
      {
        notificationType: 'ROUTINE',
        enabled: store.routineNotiEnabled,
      },
      {
        notificationType: 'BRIEFING',
        enabled: store.briefingNotiEnabled,
      },
    ],
  }

  try {
    const res = await axios.put('/v1/users/rouby-setting', payload)
  } catch (error) {
    console.error('루비 설정 업데이트 실패:', error.response ?? error)
    throw error
  }
}

export async function completeRoubySetting() {
  try {
    await axios.patch('/v1/users/onboarding/rouby/complete')
  } catch (error) {
    console.error('루비 온보딩 완료 실패:', error)
    throw error
  }
}
