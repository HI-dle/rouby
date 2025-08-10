<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <DateStartSettingForm
        :user-name="store.nickname"
        :personal-keyword="selectedPersonalFirst"
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
import { computed, ref, watchEffect } from 'vue'
import { useRouter } from 'vue-router'
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { useDateSettingForm } from '../useDateSettingForm'
import DateStartSettingForm from '@/features/onboard/components/DateStartSettingForm.vue'

const store = useUserInfoStore()
const router = useRouter()
const { form, periodOptions, hourOptions, selectedTime } = useDateSettingForm(
  store.startOfDayTime,
  'start',
)

const selectedPersonalFirst = computed(() => {
  const keywords = store.profileKeywords
  if (!keywords) return ''
  return Array.isArray(keywords) ? keywords[0] || '' : keywords
})

const timeError = ref('')
const onNextClick = () => {
  timeError.value = ''

  if (!form.value.hour || !form.value.period) {
    timeError.value = '시간을 선택해주세요!'
    return false
  }
  return true
}

const onNextLinkClick = () => {
  if (onNextClick()) {
    router.push('/onboarding/end-date-setting')
  }
}

watchEffect(() => {
  if (form.value.period && form.value.hour) {
    store.startOfDayTime = selectedTime.value
  }
})
</script>
