<template>
  <div class="main-container">
    <div class="sub-main-container">

      <NicknameSettingForm v-model="nickname" />

      <!-- 다음 단계 이동 -->
      <div class="w-full mt-10 pt-10 text-center">
        <RouterLink
          to="/다음페이지경로"
          class="text-indigo-400 underline hover:text-#6667D07A"
        >
          다음 단계로
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NicknameSettingForm from '@/features/onboard/Components/NicknameSettingForm.vue'

const nickname = ref('')
</script>
