<script setup>
import { computed } from 'vue'
import {
  ChevronDown, ChevronUp, BookOpenCheck, Box, ListTodo, Bell,
  RotateCcw, Clock, FileText, TriangleAlert,
} from 'lucide-vue-next'
import FieldError from '@/components/common/FieldError.vue'
import { useDailyTaskForm } from '../useDailyTaskForm.js'
import { days, dayEnums } from '../constants'

const props = defineProps({ task: { type: Object, required: true } })

const {
  uiState,
  getUnitLabel,
  getTaskTypeLabel,
  getAlarmLabel,
  clearError,
  handleCheckboxChange,
  handleValueChange,
} = useDailyTaskForm(props.task)

const weekdayDisplay = computed(() =>
  dayEnums.map((code, i) => ({ label: days[i], isEnabled: props.task.byDays.includes(code) }))
)
</script>

<template>
  <div class="space-y-6">
    <!-- 에러 -->
    <div v-if="uiState.errorMessage" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center justify-between">
      <span>{{ uiState.errorMessage }}</span>
      <button @click="clearError" class="text-error-color hover:text-red-700 font-bold" aria-label="오류 메시지 닫기">×</button>
    </div>

    <!-- 할일 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 rounded-lg flex items-center justify-center">
          <BookOpenCheck class="w-5 h-5 text-content-color" />
        </div>
        <span class="text-content-color font-medium">할일</span>
      </div>
      <div class="text-content-color font-medium">{{ task.title }}</div>
    </div>

    <!-- 상태 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 rounded-lg flex items-center justify-center">
          <Box class="w-5 h-5 text-content-color" />
        </div>
        <span class="text-content-color font-medium">상태</span>
      </div>

      <!-- CHECK -->
      <div v-if="task.taskType === 'CHECK'" class="relative flex items-center space-x-2">
        <div class="flex items-center w-6 justify-center">
          <div v-if="uiState.syncStatus === 'pending'" class="w-4 h-4 border-2 border-main-color/30 border-t-main-color rounded-full animate-spin"></div>
          <TriangleAlert v-else-if="uiState.syncStatus === 'failed'" class="w-4 h-4 text-error-color" />
        </div>

        <div class="relative flex items-center">
          <input
            type="checkbox"
            :checked="uiState.displayValue === 1"
            @change="handleCheckboxChange"
            class="w-5 h-5 appearance-none border-2 border-gray-300 rounded bg-transparent cursor-pointer
                   focus:outline-none focus:ring-2 focus:ring-main-color/20 checked:border-main-color checked:bg-transparent relative"
            style="background-image: none"
          />
          <svg v-if="uiState.displayValue === 1"
               class="w-3 h-3 text-main-color absolute pointer-events-none top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"
               fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"></path>
          </svg>
        </div>

        <FieldError :message="uiState.fieldErrors?.currentValue" />
      </div>

      <!-- COUNT / MINUTES -->
      <div v-else class="relative flex items-center space-x-2">
        <div class="flex items-center w-6 justify-center">
          <div v-if="uiState.syncStatus === 'pending'" class="w-4 h-4 border-2 border-main-color/30 border-t-main-color rounded-full animate-spin"></div>
          <TriangleAlert v-else-if="uiState.syncStatus === 'failed'" class="w-4 h-4 text-error-color" />
        </div>

        <div class="flex items-center rounded-lg border border-gray-200">
          <button
            @click="handleValueChange(Math.max(uiState.displayValue - 1, 0))"
            class="w-8 h-8 flex items-center justify-center hover:bg-gray-200 rounded-l-lg transition-colors"
          >
            <ChevronDown class="w-3 h-3 text-content-color" />
          </button>

          <div class="flex items-center">
            <input
              :value="uiState.displayValue"
              type="number"
              min="0"
              readonly
              class="w-12 h-8 text-center text-sm font-medium bg-transparent border-none outline-none cursor-default"
            />
            <span class="text-xs text-gray-500 min-w-6">{{ getUnitLabel() }}</span>
          </div>

          <button
            @click="handleValueChange(Math.min(uiState.displayValue + 1, 999))"
            class="w-8 h-8 flex items-center justify-center hover:bg-gray-200 rounded-r-lg transition-colors"
          >
            <ChevronUp class="w-3 h-3 text-content-color" />
          </button>
        </div>

        <FieldError :message="uiState.fieldErrors?.currentValue" />
      </div>
    </div>

    <!-- 타입 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 rounded-lg flex items-center justify-center">
          <ListTodo class="w-5 h-5 text-content-color" />
        </div>
        <span class="text-content-color font-medium">타입</span>
      </div>
      <div class="text-content-color font-medium">{{ getTaskTypeLabel() }}</div>
    </div>

    <!-- 알림 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 rounded-lg flex items-center justify-center">
          <Bell class="w-5 h-5 text-content-color" />
        </div>
        <span class="text-content-color font-medium">알림</span>
      </div>
      <div class="text-content-color font-medium">{{ getAlarmLabel() }}</div>
    </div>

    <!-- 반복 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 rounded-lg flex items-center justify-center">
          <RotateCcw class="w-5 h-5 text-content-color" />
        </div>
        <span class="text-content-color font-medium">반복</span>
      </div>
      <div class="flex items-center gap-2 text-sm">
        <span
          v-for="(day, index) in weekdayDisplay"
          :key="index"
          :class="[
            day.isEnabled ? 'bg-[#6667D0] text-white' : 'bg-gray-100 text-content-color',
            'w-6 h-6 rounded-full flex items-center justify-center text-sm font-semibold',
          ]"
        >
          {{ day.label }}
        </span>
      </div>
    </div>

    <!-- 시간 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 rounded-lg flex items-center justify-center">
          <Clock class="w-5 h-5 text-content-color" />
        </div>
        <span class="text-content-color font-medium">시간</span>
      </div>
      <div class="text-content-color font-medium">{{ task.time }}</div>
    </div>

    <!-- 메모 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 rounded-lg flex items-center justify-center">
          <FileText class="w-5 h-5 text-content-color" />
        </div>
        <span class="text-content-color font-medium">메모</span>
      </div>
      <div class="text-content-color font-medium text-right max-w-xs">
        <p class="whitespace-pre-line break-words">
          {{ task.memo || '메모가 없습니다.' }}
        </p>
      </div>
    </div>
  </div>
</template>
