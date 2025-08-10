<script setup>
import { watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { useNicknameForm } from '@/features/onboard/useNicknameForm'
import NicknameSettingForm from '@/features/onboard/components/NicknameSettingForm.vue'

const store = useUserInfoStore()
const router = useRouter()

const { nickname, nicknameError, isFocused, validateNickname } =
  useNicknameForm(store.nickname || '')

watch(nickname, (val) => {
  console.log(val)
  store.nickname = val
})

const goNext = () => {
  const isValid = validateNickname()
  if (!isValid) return
  router.push('/onboarding/health-check')
}
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container justify-center">
      <NicknameSettingForm
        v-model="nickname"
        :error="nicknameError"
        :isFocused="isFocused"
        @update:isFocused="isFocused = $event"
        @input="validateNickname"
      />
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
