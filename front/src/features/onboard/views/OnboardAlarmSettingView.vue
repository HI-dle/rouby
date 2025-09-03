<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <AlarmSettingForm
        v-model:schedule-noti-enabled="store.scheduleNotiEnabled"
        v-model:routine-noti-enabled="store.routineNotiEnabled"
        v-model:briefing-noti-enabled="store.briefingNotiEnabled"
      />

      <div class="w-full mt-10 pt-10 text-center">
        <button
          @click="onNextLinkClick"
          class="text-indigo-400 underline hover:text-#6667D07A"
        >
          다음 단계로
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { requestPermissionAndInitFCM } from '@/shared/utils/notificationUtils'
import AlarmSettingForm from '@/features/onboard/components/AlarmSettingForm.vue'
import { registerUserDevice } from '@/features/user/userService'

const store = useUserInfoStore()
const router = useRouter()

const onNextLinkClick = async () => {
  await router.push('/onboarding/calender-setting')
}

watch(
  () => [
    store.scheduleNotiEnabled,
    store.routineNotiEnabled,
    store.briefingNotiEnabled,
  ],
  async (newVals, oldVals) => {
    const allWasFalse = oldVals.every((val) => !val)
    const becameTrue = newVals.some((val, idx) => val && !oldVals?.[idx])
    if (allWasFalse && becameTrue) {
      await requestPermissionAndInitFCM()
      await registerUserDevice()
    }
  },
)
</script>
