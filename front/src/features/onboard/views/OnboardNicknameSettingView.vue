<template>
  <div class="main-container">
    <div class="sub-main-container">

      <div class="mt-32">
        <NicknameSettingForm
          ref="nicknameFormRef"
          v-model="nickname" />
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
import NicknameSettingForm from '@/features/onboard/Components/NicknameSettingForm.vue'

const nickname = ref('')
const nicknameFormRef = ref(null)
const router = useRouter()

const goNext = () => {
  // 닉네임 유효성 검사 실패 시 이동 막기
  const isValid = nicknameFormRef.value?.validate()
  if (!isValid) return

  // router 이동만 수행
  router.push('/onboarding/health-check')
}
</script>
