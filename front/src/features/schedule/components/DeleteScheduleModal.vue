<script setup>
import { cn } from '@/lib/utils'
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  message: { type: String, default: '삭제하시겠습니까?' },
})

const emit = defineEmits(['update:modelValue', 'deleteOne', 'deleteAll'])

const close = () => {
  emit('update:modelValue', false)
}

const handleDeleteOne = () => {
  emit('deleteOne')
  close()
}

const handleDeleteAll = () => {
  emit('deleteAll')
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
    <div
      :class="cn('bg-white rounded-xl shadow-lg w-full max-w-sm p-6 text-center')"
    >
      <p class="text-sm text-gray-700 mb-6">{{ message }}</p>

      <div class="flex justify-center gap-3">
        <!-- 해당 일정만 삭제 -->
        <BaseButton
          @click="handleDeleteOne"
          class="w-2/5 h-10 text-sm bg-main-color text-white"
        >
          해당 일정만 삭제
        </BaseButton>

        <!-- 이후 일정 모두 삭제 -->
        <BaseButton
          @click="handleDeleteAll"
          class="w-2/5 h-10 text-sm bg-gray-100 text-main-color border"
        >
          이후 일정 모두 삭제
        </BaseButton>
      </div>
    </div>
  </div>
</template>
