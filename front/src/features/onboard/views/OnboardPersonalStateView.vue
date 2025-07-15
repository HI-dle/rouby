<template>
  <div class="main-container">
    <div class="sub-main-container">
      <!-- 키워드 입력 폼 -->

      <div class="mt-32">
      <PersonalStateForm
        :user-name="userName"
        :selected-health="selectedHealth"
      />
      </div>

      <!-- 다음 단계 이동 -->
      <div class="w-full mt-10 pt-10 text-center">
        <RouterLink
          to="/다음페이지경로"
          class="text-indigo-400 underline hover:text-indigo-400"
        >
          다음 단계로
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import PersonalStateForm from '@/features/onboard/Components/PersonalStateForm.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

const store = useOnboardStore()
const selectedHealth = computed(() => store.selectedHealth)
const userName = computed(() => store.userName)
const exampleKeywords = ref([]) // 유동적으로 받아오기 가능
</script>
