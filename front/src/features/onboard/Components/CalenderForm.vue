<template>
  <div class="text-center mt-20 space-y-6 text-main-color">
    <div>
      <p class="text-base">제가 미리 알면 좋은 일정이 있을까요?</p>
      <p class="text-sm text-[#6667D07A] mt-2">더 멋진 하루를 만들어 드릴게요!</p>
      <p class="text-sm text-[#6667D07A] mt-2">일정 수정은 나중에도 할 수 있어요</p>
    </div>

  </div>
</template>

<script setup>
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

const store = useOnboardStore()

const onNextClick = async () => {
  return true
}

defineExpose({ onNextClick })
</script>

