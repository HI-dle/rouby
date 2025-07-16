
<template>
  <div class="text-center mt-12 space-y-6 w-full text-main-color">
    <div>
      <p class="text-base font-semibold">만나서 반가워요!</p>
      <p class="text-base font-semibold">제가 뭐라고 부르면 될까요?</p>
      <p class="text-sm text-#6667D07A mt-2">루비와 함께할 분의 이름을 알고싶어요!</p>
    </div>

    <!-- 문장과 inline 인풋 -->
    <div class="text-base mt-6 text-main-color">
      나를
      <input
        v-model="nickname"
        type="text"
        placeholder="루비"
        :style="{
          width: nickname.length <= 4 ? '96px' : `${nickname.length + 1}ch`,
          minWidth: '96px',
          maxWidth: '200px',
          boxShadow: nicknameError && isFocused ? '0 0 0 3px rgba(255, 72, 66, 0.3)' : '',
          transition: 'width 0.2s ease',
        }"
        class="inline-block text-center mx-2 px-3 py-1 rounded-full bg-white border text-main-color placeholder:text-placeholder-color text-base outline-none focus:ring-2 transition shadow-sm"
        :class="nicknameError ? 'border-error-color focus:ring-error-color' : 'border-border-color focus:ring-[#B6A6FF]'"
        @input="validateNickname"
        @focus="isFocused = true"
        @blur="isFocused = false"
      />
      (이)라고 불러줘
    </div>

    <FieldError v-if="nicknameError" :message="nicknameError" class="mt-2" />
  </div>
</template>

<script setup>
import { computed, watch } from 'vue'
import FieldError from '@/components/common/FieldError.vue'
import { useNicknameForm } from '@/features/onboard/useNicknameForm.js'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore'

const props = defineProps({
  modelValue: String,
})
const emit = defineEmits(['update:modelValue'])

const {
  nickname: internalNickname,
  nicknameError,
  isFocused,
  validateNickname,
} = useNicknameForm()

// 💡 computed로 양방향 바인딩 연결
const nickname = computed({
  get() {
    return props.modelValue
  },
  set(val) {
    emit('update:modelValue', val)
    watch(() => props.modelValue, (newVal) => {
        if (newVal !== internalNickname.value) {
            internalNickname.value = newVal
            }
      },
      { immediate: true })
  },
})

// store에 반영
const store = useOnboardStore()
  watch(nickname, (val, oldVal) => {
    if (val !== oldVal) {
      store.userName = val
    }
  })

// 부모가 호출할 수 있도록 expose
defineExpose({ validate: validateNickname })
</script>




