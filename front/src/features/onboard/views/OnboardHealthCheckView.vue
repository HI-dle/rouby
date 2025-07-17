<template>
  <div class="main-container">
    <div class="sub-main-container">
      <div class="mt-32">
      <HealthCheckForm ref="healthFormRef" :user-name="userName" />
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
import { ref,computed } from 'vue'
import HealthCheckForm from '@/features/onboard/Components/HealthCheckForm.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'
import { useRouter } from 'vue-router'

const store = useOnboardStore()
const userName = computed(() => store.userName)

const router = useRouter()

const healthFormRef = ref(null)

const onNextLinkClick = async () => {
  if (!healthFormRef.value) return

  const success = await healthFormRef.value.onNextClick()
  if (success) {
    await router.push('/onboarding/profile-setting')
  }
}
</script>
