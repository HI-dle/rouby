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
import AlarmSettingForm from '@/features/onboard/components/AlarmSettingForm.vue'
import { requestPermissionAndInitFCM } from '@/shared/firebase/config'

import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { useRouter } from 'vue-router'

const store = useUserInfoStore()
const router = useRouter()

const onNextLinkClick = async () => {
  requestPermissionAndInitFCM()
  await router.push('/onboarding/calender-setting')
}
</script>
