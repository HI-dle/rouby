<script setup>
import { cn } from '@/lib/utils'
import BaseButton from './BaseButton.vue'

const props = defineProps({
  modelValue: { type: Boolean },
  title: { type: String, default: '' },
  message: { type: String, default: '알림 메세지' },
  buttonText: {
    type: String,
    default: '확인',
  },
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
    role="dialog"
  >
    <div
      :class="[
        cn('bg-white rounded-xl shadow-lg w-full max-w-sm w-2/3 p-6 text-center', props.class),
      ]"
    >
      <h2 v-if="title" class="text-lg font-semibold text-main-color mb-4">{{ title }}</h2>
      <p class="text-sm text-gray-700 mb-6">{{ message }}</p>
      <BaseButton @click="close" :class="['text-sm w-2/5 h-10', props.buttonClass]">
        {{ buttonText }}
      </BaseButton>
    </div>
  </div>
</template>
