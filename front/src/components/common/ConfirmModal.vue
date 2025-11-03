<script setup>
import { cn } from '@/lib/utils'
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  message: { type: String, default: '정말 진행하시겠습니까?' },
  actions: {
    type: Array,
    default: () => [
      { label: '확인', type: 'primary', handler: () => {} },
      { label: '취소', type: 'secondary', handler: () => {} },
    ],
  },
})

const emit = defineEmits(['update:modelValue'])

const close = () => emit('update:modelValue', false)

const handleAction = (action) => {
  action.handler()
  close()
}
</script>

<template>
  <div
    v-if="modelValue"
    class="fixed inset-0 z-[9999] flex items-center justify-center bg-black bg-opacity-50"
    @click.self="close"
    role="dialog"
  >
    <div :class="cn('bg-white rounded-xl shadow-lg w-full max-w-sm p-6 text-center')">
      <p class="text-sm text-gray-700 mb-6">{{ message }}</p>

      <div class="flex justify-center gap-3">
        <BaseButton
          v-for="(action, index) in actions"
          :key="index"
          @click="handleAction(action)"
          :class="cn(
            'w-2/5 h-10 text-sm',
            action.type === 'primary'
              ? 'bg-main-color text-white'
              : 'bg-gray-100 text-main-color border'
          )"
        >
          {{ action.label }}
        </BaseButton>
      </div>
    </div>
  </div>
</template>
