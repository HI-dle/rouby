<template>
  <div class="main-container">
    <div class="sub-main-container">

      <div class="mt-32">
        <DateEndSettingForm
          ref="dateSettingFormRef"
          :user-name="userName"
          :start-day-time="startOfDayTime"
        />
      </div>

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
import { computed, ref } from 'vue'
import DateEndSettingForm from '@/features/onboard/Components/DateEndSettingForm.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'
import { useRouter } from 'vue-router'
import { updateUserInfo, completeUserSetting } from '@/features/onboard/onboardUserSettingApi.js'

const store = useOnboardStore()
const startOfDayTime = computed(() => store.startOfDayTime)
const userName = computed(() => store.userName)
const router = useRouter()

const dateSettingFormRef = ref(null)

const onNextLinkClick = async () => {
  if (!dateSettingFormRef.value) return


  const success = dateSettingFormRef.value.onNextClick()
  if (!success) return

  try {

    await updateUserInfo()
    await completeUserSetting()
    await router.push('/onboarding/speech-setting')
  } catch (e) {
    alert('설정 저장에 실패했습니다. 다시 시도해주세요.')
    console.error(e)
  }
}
</script>

