<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <CalendarForm ref="CalendarFormRef" />

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
import CalendarForm from '@/features/onboard/components/CalenderForm.vue'
import { completeRoubySetting, updateRoubySetting } from '../onboardUserSerivce'
import { useRouter } from 'vue-router'

const router = useRouter()

const onNextLinkClick = async () => {
  try {
    await updateRoubySetting()
    await completeRoubySetting()

    await router.push('/')
  } catch (e) {
    alert('설정 저장에 실패했어요. 다시 시도해주세요.')
  }
}
</script>
