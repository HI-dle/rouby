<script setup>
import { Loader } from 'lucide-vue-next'

// props로 크기와 선 굵기를 조절할 수 있게 합니다.
defineProps({
  size: {
    type: [Number, String],
    default: 24, // 기본 크기
  },
  strokeWidth: {
    type: [Number, String],
    default: 3, // 기본 선 굵기 (Lucide 기본값은 2이지만, 로더는 좀 더 굵게 할 수 있습니다)
  },
})
</script>

<template>
  <div class="loading-spinner">
    <Loader :size="size" :stroke-width="strokeWidth" class="spinner-icon" />
  </div>
</template>

<style scoped>
/*
  CSS 애니메이션을 사용하여 아이콘을 회전시킵니다.
*/
.spinner-icon {
  animation: spin 1s linear infinite; /* 1초 동안, 선형으로, 무한 반복 회전 */
  color: white;
}

/*
  Keyframes는 애니메이션의 시작(0%)과 끝(100%)을 정의합니다.
*/
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 로딩 스피너 컨테이너를 필요에 따라 설정할 수 있습니다. */
.loading-spinner {
  display: inline-block;
  line-height: 1; /* 아이콘이 라인에서 정렬되도록 */
}
</style>
