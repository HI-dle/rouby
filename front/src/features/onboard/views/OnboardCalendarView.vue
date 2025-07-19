<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="mt-32">
      <CalendarForm ref="CalendarFormRef" />
      </div>

      <div class="w-full mt-10 pt-10 text-center">
        <button
          @click="onNextLinkClick"
          class="text-indigo-400 underline hover:text-#6667D07A"
        >
          시작하기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import CalendarForm from '@/features/onboard/Components/CalenderForm.vue'
import { ref } from 'vue'
import router from '@/router/index.js'

const CalendarFormRef = ref(null)

const onNextLinkClick = async () => {
  if (!CalendarFormRef.value) return

  const success = await CalendarFormRef.value.onNextClick()
  if (success) {
    await router.push('/onboarding/다음')
  }
}
</script>
