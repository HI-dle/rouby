<template>
  <div class="main-container">
    <div class="sub-main-container">

      <div class="mt-32">
        <NicknameSettingForm v-model="nickname" />
      </div>

      <!-- 다음 단계 이동 -->
      <div class="w-full mt-10 pt-10 text-center">
        <button
          @click="goNext"
          class="text-indigo-400 underline hover:text-#6667D07A"
        >
          다음 단계로
        </button>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'
import NicknameSettingForm from '@/features/onboard/Components/NicknameSettingForm.vue'

const nickname = ref('')
const router = useRouter()
const store = useOnboardStore()

const goNext = () => {
  if (!nickname.value.trim()) {
    alert('닉네임을 입력해주세요!')
    return
  }
  store.userName = nickname.value.trim() // Pinia에 저장
  router.push('/onboarding/health-check')           // 다음 페이지 이동
}
</script>
