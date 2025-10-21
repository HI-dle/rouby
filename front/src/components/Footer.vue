<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

import {
  Calendar,
  ClipboardList,
  Clock3,
  User,
  MoreHorizontal,
  Plus,
  Gem,
} from 'lucide-vue-next'
import { useActivePath } from '@/shared/composable/useActivePath'

const route = useRoute()
const { isActive } = useActivePath()

const createOrModify = ['create', 'modify']
const isNotCreateOrModify = () =>
  !createOrModify.some((p) => route.path.includes(p))

const isMoreClicked = ref(false)
const toggleMoreOptions = () => {
  isMoreClicked.value = !isMoreClicked.value
}
</script>

<template>
  <footer
    role="contentinfo"
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
      to="/routine-task"
      class="flex justify-center items-center"
      :class="isActive('/routine-task') ? 'text-main-color' : 'text-gray-400'"
    >
      <ClipboardList class="w-6 h-6" />
    </RouterLink>

    <RouterLink
      to="/notification"
      class="flex justify-center items-center"
      :class="isActive('/notification') ? 'text-main-color' : 'text-gray-400'"
    >
      <Clock3 class="w-6 h-6" />
    </RouterLink>

    <RouterLink
      to="/user/mypage"
      class="flex justify-center items-center"
      :class="isActive('/user/mypage') ? 'text-main-color' : 'text-gray-400'"
    >
      <User class="w-6 h-6" />
    </RouterLink>

    <!-- + 버튼 -->
    <!-- <RouterLink
      to="/create"
      class="absolute -top-16 right-6 bg-main-color text-white w-12 h-12 rounded-full shadow-lg flex items-center justify-center"
      aria-label="새 항목 추가"
    >
      <Plus class="w-6 h-6" />
    </RouterLink>
     -->

    <!-- ... 버튼 -->
    <div
      v-if="isNotCreateOrModify()"
      class="absolute -top-14 right-6 bg-main-color text-white w-10 h-10 rounded-full shadow-lg flex items-center justify-center"
      aria-label="추가 선택지"
      @click="toggleMoreOptions"
    >
      <MoreHorizontal class="w-6 h-6" />
    </div>

    <div
      v-if="isMoreClicked"
      class="absolute -top-[11rem] right-4 w-36 h-20 flex flex-col items-center justify-center"
    >
      <RouterLink
        to="/assistant/recommendation/request"
        class="flex justify-center items-center w-full h-8 bg-main-color rounded-full shadow-lg"
        @click="isMoreClicked = false"
      >
        <div
          class="flex justify-between items-center w-full h-8 text-white px-[9px]"
        >
          <Gem class="size-5" />
          <span>루틴 추천받기</span>
        </div>
      </RouterLink>
      <RouterLink
        to="/assistant/feedback/daily/request"
        class="flex justify-center items-center w-full h-8 mt-1 bg-main-color rounded-full shadow-lg"
        @click="isMoreClicked = false"
      >
        <div
          class="flex justify-between items-center w-full h-8 text-white px-[9px]"
        >
          <Gem class="size-5" />
          <span>하루 마무리하기</span>
        </div>
      </RouterLink>
      <RouterLink
        to="/assistant/feedback/daily/list"
        class="flex justify-center items-center w-full h-8 mt-1 bg-main-color rounded-full shadow-lg"
        @click="isMoreClicked = false"
      >
        <div
          class="flex justify-between items-center w-full h-8 text-white px-[9px]"
        >
          <Gem class="size-5" />
          <span>피드백 보기</span>
        </div>
      </RouterLink>
      <RouterLink
        to="/assistant/briefing/daily"
        class="flex justify-center items-center w-full h-8 mt-1 bg-main-color rounded-full shadow-lg"
        @click="isMoreClicked = false"
      >
        <div
          class="flex justify-between items-center w-full h-8 text-white px-[9px]"
        >
          <Gem class="size-5" />
          <span>브리핑 보기</span>
        </div>
      </RouterLink>
    </div>
  </footer>
</template>
