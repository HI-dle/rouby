import axios from '@/api/axios'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore.js'

export async function updateRoubySetting() {
  const store = useOnboardStore()

  const payload = {
    communicationTone: store.speechType,
    notificationSettings: [
      {
        notificationType: 'SCHEDULE',
        enabled: store.scheduleAlarm,
      },
      {
        notificationType: 'ROUTINE',
        enabled: store.routineAlarm,
      },
      {
        notificationType: 'BRIEFING',
        enabled: store.briefingAlarm,
      },
    ],
  }

  try {
    const res = await axios.put('/v1/users/rouby-setting', payload)
    console.log('업데이트 성공:', res.data)
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
