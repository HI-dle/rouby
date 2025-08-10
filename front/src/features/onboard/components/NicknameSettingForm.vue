<script setup>
import { computed } from 'vue'
import FieldError from '@/components/common/FieldError.vue'

const props = defineProps({
  modelValue: String,
  error: String,
  isFocused: Boolean,
})
const emit = defineEmits(['update:modelValue', 'update:isFocused', 'input'])

const inputStyle = computed(() => ({
  width:
    props.modelValue.length <= 4 ? '96px' : `${props.modelValue.length + 1}ch`,
  minWidth: '96px',
  maxWidth: '200px',
  boxShadow:
    props.error && props.isFocused ? '0 0 0 1px rgba(255, 72, 66, 0.3)' : '',
  transition: 'width 0.2s ease',
}))
</script>

<template>
  <div class="text-center space-y-6 w-full text-main-color">
    <div>
      <p class="text-base">만나서 반가워요!</p>
      <p class="text-base">제가 뭐라고 부르면 될까요?</p>
      <p class="text-sm text-#6667D07A mt-2">
        루비와 함께할 분의 이름을 알고싶어요!
      </p>
    </div>

    <div class="text-base mt-6 text-main-color">
      나를
      <input
        :value="modelValue"
        @input="(emit('update:modelValue', $event.target.value), emit('input'))"
        type="text"
        placeholder="루비"
        :style="inputStyle"
        class="inline-block text-center mx-2 px-3 py-1 rounded-full bg-white border text-main-color placeholder:text-placeholder-color text-base outline-none focus:ring-1 transition shadow-sm"
        :class="
          error
            ? 'border-error-color focus:ring-error-color'
            : 'border-border-color focus:ring-[#B6A6FF]'
        "
        @focus="emit('update:isFocused', true)"
        @blur="emit('update:isFocused', false)"
      />
      (이)라고 불러줘
    </div>

    <FieldError v-if="error" :message="error" class="mt-2" />
  </div>
</template>
