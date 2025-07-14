<script setup>
const props = defineProps({
  icon: { type: String, required: true },       // 아이콘 이미지 경로
  alt: { type: String, default: 'Social Login' }, // 대체 텍스트
})

const emit = defineEmits(['click'])
</script>

<template>
  <button
    @click="$emit('click')"
    class="w-[100px] h-[56px] bg-white text-black border border-[#D5C8FF] rounded-2xl hover:bg-violet-50 flex items-center justify-center transition"
  >
    <img :src="icon" :alt="alt" class="h-6" />
  </button>
</template>
