<template>
  <div class="main-container">
    <div class="sub-main-container space-y-6 bg-white">
      <!-- 페이지 헤더 -->
      <div class="flex items-center justify-between">
        <button class="w-8 h-8 flex items-center justify-center">
          <ChevronDown class="w-6 h-6 text-content-color" />
        </button>
        <button class="w-8 h-8 flex items-center justify-center">
          <Pencil class="w-5 h-5 text-content-color" />
        </button>
      </div>

      <!-- 날짜 헤더 -->
      <div class="text-center">
        <h1 class="text-xl font-medium text-main-color">{{ formatDateHeader(dailyTask.date) }}</h1>
      </div>

      <!-- 데일리 태스크 폼 -->
      <DailyTaskForm :task="dailyTask" />
    </div>
  </div>
</template>

<script setup>
import { ChevronDown, Pencil } from 'lucide-vue-next'
import DailyTaskForm from '../components/DailyTaskForm.vue'
import { formatDateHeader } from '@/shared/utils/dateTimeUtils.js'

// 더미 데이터 (실제로는 props나 router params에서 받아올 것)
const dailyTask = {
  id: null,
  routineTaskId: 1,
  title: "달리기 30분 이상하기",
  date: "2025-07-28",
  taskType: "CHECK", // "CHECK", "COUNT", "MINUTES"
  currentValue: 0, // 서버의 실제 값
  time: "오전 7:00",
  alarmOffsetMinutes: 5,
  byDays: ['MO', 'TU', 'WE', 'TH', 'FR'],
  memo: "입력된값"
}


</script>
