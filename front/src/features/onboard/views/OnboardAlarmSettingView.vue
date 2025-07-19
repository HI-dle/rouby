<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="mt-32">
      <AlarmSettingForm ref="AlarmFormRef" />
      </div>

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
import AlarmSettingForm from '@/features/onboard/Components/AlarmSettingForm.vue'
import { ref } from 'vue'
import router from '@/router/index.js'

const AlarmFormRef = ref(null)

const onNextLinkClick = async () => {
  if (!AlarmFormRef.value) return

  const success = await AlarmFormRef.value.onNextClick()
  if (success) {
    await router.push('/onboarding/Calender-setting')
  }
}
</script>
