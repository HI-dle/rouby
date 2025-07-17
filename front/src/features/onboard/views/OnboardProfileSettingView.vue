<template>
  <div class="main-container">
    <div class="sub-main-container">
      <!-- 키워드 입력 폼 -->

      <div class="mt-32">
      <PersonalStateForm
        ref="personalRef"
        :user-name="userName"
        :selected-health="selectedHealth"
      />
      </div>

      <!-- 다음 단계 이동 -->
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
import { computed, ref } from 'vue'
import PersonalStateForm from '@/features/onboard/Components/ProfileSettingForm.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'
import { useRouter } from 'vue-router'

const store = useOnboardStore()
const selectedHealth = computed(() => store.selectedHealth)
const userName = computed(() => store.userName)
const exampleKeywords = ref([]) // 유동적으로 받아오기 가능
const router = useRouter()

const personalRef = ref(null)

const onNextLinkClick = () => {
  if (!personalRef.value) return

  const success = personalRef.value.onNextClick()
  if (success) {
    router.push('/onboarding/date-setting')
  }
}
</script>
