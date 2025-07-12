<script setup>
const props = defineProps({
  type: {
    type: String,
    default: 'button', // 기본은 'button', 폼 제출용일 경우 'submit'을 명시적으로 전달
  },
  class: {
    type: String,
    default: '',
  },
})
</script>

<template>
  <button
    :type="type"
    @click="$emit('click')"
    :class="[
      'w-full py-3 rounded-2xl text-white text-base font-semibold bg-gradient-to-r from-button-from to-button-to hover:from-[#546ac9] hover:to-[#6a4093] transition',
      props.class,
    ]"
  >
    <slot>로그인</slot>
  </button>
</template>
