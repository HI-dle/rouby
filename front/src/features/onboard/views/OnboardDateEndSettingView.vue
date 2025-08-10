<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <DateEndSettingForm
        :user-name="store.nickname"
        :start-day-time="store.startOfDayTime"
        v-model:period="form.period"
        v-model:hour="form.hour"
        :period-options="periodOptions"
        :hour-options="hourOptions"
      />

      <!-- 다음 단계 이동 -->
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
import { ref, watchEffect } from 'vue'
import { useRouter } from 'vue-router'
import DateEndSettingForm from '@/features/onboard/components/DateEndSettingForm.vue'
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { useDateSettingForm } from '../useDateSettingForm'
import {
  updateUserInfo,
  completeUserSetting,
} from '@/features/onboard/onboardUserSerivce.js'

const router = useRouter()
const store = useUserInfoStore()

const { form, periodOptions, hourOptions, selectedTime } = useDateSettingForm(
  store.endOfDayTime,
  'end',
)

const timeError = ref('')
const onNextClick = () => {
  timeError.value = ''

  if (!form.value.hour || !form.value.period) {
    timeError.value = '시간을 선택해주세요!'
    return false
  }
  return true
}

const onNextLinkClick = async () => {
  if (!onNextClick()) return

  try {
    await updateUserInfo()
    await completeUserSetting()
    await router.push('/onboarding/speech-setting')
  } catch (e) {
    alert('설정 저장에 실패했습니다. 다시 시도해주세요.')
    console.error(e)
  }
}

watchEffect(() => {
  if (form.value.period && form.value.hour) {
    store.endOfDayTime = selectedTime.value
  }
})
</script>
