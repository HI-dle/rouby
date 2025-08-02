<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="mt-32">
        <NicknameSettingForm
          ref="nicknameFormRef"
          v-model="nickname" />
      </div>

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
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import NicknameSettingForm from '@/features/onboard/Components/NicknameSettingForm.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

const store = useOnboardStore()
const router = useRouter()

const nickname = ref(store.userName)

watch(
  () => store.userName,
  (val) => {
    if (val !== nickname.value) nickname.value = val
  }
)

watch(
  nickname,
  (val) => {
    if (val !== store.userName) store.userName = val
  }
)

const nicknameFormRef = ref(null)

const goNext = () => {
  const isValid = nicknameFormRef.value?.validate()
  if (!isValid) return
  router.push('/onboarding/health-check')
}
</script>
