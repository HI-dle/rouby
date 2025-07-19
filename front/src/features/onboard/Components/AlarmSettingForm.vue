<template>
  <div class="text-center mt-20 space-y-6 text-main-color">
    <div>
      <p class="text-base">루틴 알림 기능을 지원해요.</p>
      <p class="text-base">사용하시겠어요?</p>
      <p class="text-sm text-[#6667D07A] mt-2">알림 설정은 마이페이지에서 수정할 수 있어요.</p>
    </div>

    <!-- 알림 설정 스위치 -->
    <div class="space-y-4 mt-10">
      <div class="flex items-center justify-between bg-white px-4 py-3 rounded-full shadow-sm w-[320px] mx-auto">
        <span class="text-sm font-medium">일정 시작 전 알림</span>
        <BaseToggle v-model="store.scheduleAlarm" />
      </div>
      <div class="flex items-center justify-between bg-white px-4 py-3 rounded-full shadow-sm w-[320px] mx-auto">
        <span class="text-sm font-medium">루틴 시작 전 알림</span>
        <BaseToggle v-model="store.routineAlarm" />
      </div>
      <div class="flex items-center justify-between bg-white px-4 py-3 rounded-full shadow-sm w-[320px] mx-auto">
        <span class="text-sm font-medium">하루 시작 브리핑 알림</span>
        <BaseToggle v-model="store.briefingAlarm" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'
import BaseToggle from '@/components/common/ToggleSwitch.vue'

const store = useOnboardStore()

const onNextClick = () => {
  return true
}
defineExpose({ onNextClick })
</script>
