import { useAuthStore } from '@/stores/useAuthStore'
import { useDatePickStore } from '@/stores/useDatePickStore'
import { useScheduleStore } from '@/stores/useScheduleStore'
import { useUserInfoStore } from '@/stores/useUserInfoStore'

export const resetAllStores = () => {
  const authStore = useAuthStore()
  const datePickStore = useDatePickStore()
  const scheduleStore = useScheduleStore()
  const userInfoStore = useUserInfoStore()

  authStore.reset()
  datePickStore.reset()
  scheduleStore.reset()
  userInfoStore.reset()
}
