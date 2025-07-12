<script setup>
import { cn } from '@/lib/utils'
import BaseButton from './BaseButton.vue'

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  title: { type: String, default: '' },
  message: { type: String, default: '알림 메세지' },
  class: {
    type: String,
    default: '',
  },
  buttonClass: {
    type: String,
    default: '',
  },
})
const emit = defineEmits(['update:modelValue'])
const close = () => {
  emit('update:modelValue', false)
}
</script>
<template>
  <div
    v-if="modelValue"
    class="fixed inset-0 z-[9999] flex items-center justify-center bg-black bg-opacity-50"
    @click.self="close"
  >
    <div
      :class="[cn('bg-white rounded-xl shadow-lg w-full max-w-sm p-6 text-center', props.class)]"
    >
      <h2 class="text-lg font-semibold text-main-color mb-4">{{ title }}</h2>
      <p class="text-sm text-gray-700 mb-6">{{ message }}</p>
      <BaseButton
        @click="$emit('update:modelValue', false)"
        :class="['text-sm w-2/5 h-10', props.buttonClass]"
      >
        확인
      </BaseButton>
    </div>
  </div>
</template>
