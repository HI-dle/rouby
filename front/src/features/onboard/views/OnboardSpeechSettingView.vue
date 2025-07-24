<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="mt-32">
      <SpeechSettingForm ref="SpeechFormRef" />
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
import SpeechSettingForm from '@/features/onboard/Components/SpeechSettingForm.vue'
import { ref } from 'vue'
import router from '@/router/index.js'

const SpeechFormRef = ref(null)

const onNextLinkClick = async () => {
  if (!SpeechFormRef.value) return

  const success = await SpeechFormRef.value.onNextClick()
  if (success) {
    await router.push('/onboarding/alarm-setting')
  }
}
</script>
