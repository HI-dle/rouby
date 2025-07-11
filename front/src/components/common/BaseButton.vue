<!-- src/components/common/BaseButton.vue -->
<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  /** button 태그의 type (button, submit 등) */
  type: {
    type: String,
    default: 'button'
  },
  /** 비활성화 상태 */
  disabled: {
    type: Boolean,
    default: false
  },
  /** 그라데이션 시작 색상 (Tailwind JIT 임의값) */
  gradientFrom: {
    type: String,
    default: '#667EEA'
  },
  /** 그라데이션 끝 색상 */
  gradientTo: {
    type: String,
    default: '#764BA2'
  },
  heightClass: {
    type: String,
    default: 'h-12'
  },
  /** 추가로 쓰고 싶은 CSS 클래스 */
  customClass: {
    type: [String, Array, Object],
    default: ''
  }
})

const emit = defineEmits(['click'])
</script>

<template>
  <button
    :type="type"
    :disabled="disabled"
    @click="emit('click', $event)"
    :class="[
      'px-4',
      heightClass,                              /* 높이 고정 */
      `bg-gradient-to-r from-[${gradientFrom}] to-[${gradientTo}]`,
      'text-white',
      'rounded-[16px]',                         /* border-radius:16px */
      'hover:opacity-90',
      'disabled:opacity-50',
      'whitespace-nowrap',
      customClass                               /* 추가 클래스 */
    ]"
  >
    <!-- 버튼 내부 텍스트나 아이콘을 슬롯으로 받음 -->
    <slot />
  </button>
</template>
