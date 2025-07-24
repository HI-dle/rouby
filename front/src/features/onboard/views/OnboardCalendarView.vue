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
import router from '@/router'
import {
  updateRoubySetting,
  completeRoubySetting,
} from '@/features/onboard/onboardRoubySettingApi.js'

const CalendarFormRef = ref(null)

const onNextLinkClick = async () => {
  if (!CalendarFormRef.value) return

  const success = await CalendarFormRef.value.onNextClick()
  if (success) {
    try {
      await updateRoubySetting()
      await completeRoubySetting()
      await router.push('/')
    } catch (e) {
      alert('설정 저장에 실패했어요. 다시 시도해주세요.')
    }
  }
}
</script>

