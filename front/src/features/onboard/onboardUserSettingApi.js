import axios from '@/api/axios'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore.js'

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

export async function updateUserInfo() {
  const store = useOnboardStore()

  const payload = {
    nickname: store.userName,
    healthStatusKeywords: Array.isArray(store.selectedHealth) ? store.selectedHealth : [],
    profileKeywords: Array.isArray(store.personalKeyword) ? store.personalKeyword : [],
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
