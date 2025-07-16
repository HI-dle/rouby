<template>
  <div class="main-container">
    <div class="sub-main-container">

      <div class="mt-32">
        <DateSettingForm
          ref="dateSettingFormRef"
          :user-name="userName"
          :personal-keyword="personalKeyword"
        />
      </div>

      <!-- 다음 단계 이동 -->
      <div class="w-full mt-10 pt-10 text-center">
        <RouterLink
          to="다음은 어디일까"
          class="text-indigo-400 underline hover:text-#6667D07A"
          @click.prevent="onNextLinkClick"
        >
          다음 단계로
        </RouterLink>
      </div>
    </div>
  </div>
</template>


<script setup>
import { computed, ref } from 'vue'
import DateSettingForm from '@/features/onboard/Components/DateSettingForm.vue'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'
import { useRouter } from 'vue-router'

const store = useOnboardStore()
const personalKeyword = computed(() => store.personalKeyword)
const userName = computed(() => store.userName)
const router = useRouter()

const dateSettingFormRef = ref(null)

const onNextLinkClick = () => {
  if (!dateSettingFormRef.value) return

  const success = dateSettingFormRef.value.onNextClick()
  if (success) {
    router.push('/다음페이지로')
  }
}

</script>
