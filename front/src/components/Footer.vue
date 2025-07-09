<script setup>
import { useRoute } from 'vue-router'
import { Calendar, ClipboardList, Clock3, User, MoreHorizontal, Plus } from 'lucide-vue-next'

const route = useRoute()

// 현재 경로와 일치하는지 확인하는 함수
const isActive = (path) => route.path.startsWith(path)
</script>

<template>
  <footer
    class="w-full lg:max-w-screen-md fixed mx-auto bottom-0 left-0 right-0 bg-white border-t shadow-md flex justify-between items-center px-4 sm:px-6 py-3 z-50 rounded-t-3xl safe-area-inset-bottom"
  >
    <RouterLink
      to="/schedule"
      class="flex justify-center items-center"
      :class="isActive('/schedule') ? 'text-main-color' : 'text-gray-400'"
    >
      <Calendar class="w-6 h-6" />
    </RouterLink>

    <RouterLink
      to="/routine"
      class="flex justify-center items-center"
      :class="isActive('/routine') ? 'text-main-color' : 'text-gray-400'"
    >
      <ClipboardList class="w-6 h-6" />
    </RouterLink>

    <RouterLink
      to="/history"
      class="flex justify-center items-center"
      :class="isActive('/history') ? 'text-main-color' : 'text-gray-400'"
    >
      <Clock3 class="w-6 h-6" />
    </RouterLink>

    <RouterLink
      to="/mypage"
      class="flex justify-center items-center"
      :class="isActive('/mypage') ? 'text-main-color' : 'text-gray-400'"
    >
      <User class="w-6 h-6" />
    </RouterLink>

    <!-- ... 버튼 -->
    <div
      class="absolute -top-16 right-6 bg-main-color text-white w-12 h-12 rounded-full shadow-lg flex items-center justify-center"
      aria-label="새 항목 추가"
    >
      <MoreHorizontal class="w-6 h-6" />
    </div>
    <!-- + 버튼 -->
    <!-- <RouterLink
      to="/create"
      class="absolute -top-16 right-6 bg-main-color text-white w-12 h-12 rounded-full shadow-lg flex items-center justify-center"
      aria-label="새 항목 추가"
    >
      <Plus class="w-6 h-6" />
    </RouterLink>
     -->
  </footer>
</template>
