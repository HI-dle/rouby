<template>
  <div class="main-container">
    <div class="sub-main-container space-y-6">
      <!-- 페이지 헤더 -->
      <div class="flex items-center justify-between">
        <button class="w-8 h-8 flex items-center justify-center">
          <ChevronDown class="w-6 h-6 text-content-color" />
        </button>
        <button class="w-8 h-8 flex items-center justify-center">
          <Pencil class="w-5 h-5 text-content-color" />
        </button>
      </div>

      <!-- 에러 메시지 표시 -->
      <div v-if="uiState.errorMessage"
           class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center justify-between">
        <span>{{ uiState.errorMessage }}</span>
        <button @click="clearError" class="text-red-500 hover:text-red-700 font-bold">×</button>
      </div>

      <!-- 날짜 헤더 -->
      <div class="text-center">
        <h1 class="text-xl font-medium text-main-color">{{ formattedDateHeader }}</h1>
      </div>

      <!-- 폼 필드들 -->
      <div class="space-y-4">
        <!-- 할일 -->
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-lg flex items-center justify-center">
              <BookOpenCheck class="w-5 h-5 text-content-color" />
            </div>
            <span class="text-gray-700 font-medium">할일</span>
          </div>
          <div class="text-gray-900 font-medium">{{ dailyTask.title }}</div>
        </div>

        <!-- 상태 -->
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-lg flex items-center justify-center">
              <Box class="w-5 h-5 text-content-color" />
            </div>
            <span class="text-gray-700 font-medium">상태</span>
          </div>

          <!-- CHECK 타입: 간단한 체크박스만 -->
          <div v-if="dailyTask.taskType === 'CHECK'" class="relative flex items-center space-x-2">
            <!-- 저장 상태 표시 (왼쪽) -->
            <div class="flex items-center w-6 justify-center">
              <!-- 저장 중 -->
              <div v-if="uiState.syncStatus === 'pending'" class="w-4 h-4 border-2 border-main-color/30 border-t-main-color rounded-full animate-spin"></div>
              <!-- 저장 실패 -->
              <TriangleAlert v-else-if="uiState.syncStatus === 'failed'" class="w-4 h-4 text-red-400" />
            </div>

            <div class="relative flex items-center" @mouseleave="handleCheckboxBlur">
              <input
                type="checkbox"
                :checked="uiState.displayValue === 1"
                @change="handleCheckboxChange"
                :disabled="false"
                class="w-5 h-5 appearance-none border-2 border-gray-300 rounded bg-transparent cursor-pointer focus:outline-none focus:ring-2 focus:ring-main-color/20 checked:border-main-color checked:bg-transparent relative"
                style="background-image: none;"
              />
              <!-- 커스텀 체크 아이콘 -->
              <svg
                v-if="uiState.displayValue === 1"
                class="w-3 h-3 text-main-color absolute pointer-events-none"
                style="left: 4px; top: 4px;"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"></path>
              </svg>
            </div>

            <!-- 필드별 에러 메시지 -->
            <FieldError :message="uiState.fieldErrors?.currentValue" />
          </div>

          <!-- COUNT, MINUTES 타입: 숫자 입력 -->
          <div v-else class="relative flex items-center space-x-2">
            <!-- 저장 상태 표시 (왼쪽) -->
            <div class="flex items-center w-6 justify-center">
              <!-- 저장 중 -->
              <div v-if="uiState.syncStatus === 'pending'" class="w-4 h-4 border-2 border-main-color/30 border-t-main-color rounded-full animate-spin"></div>
              <!-- 저장 실패 -->
              <TriangleAlert v-else-if="uiState.syncStatus === 'failed'" class="w-4 h-4 text-red-400" />
            </div>

            <div
              class="flex items-center rounded-lg border border-gray-200"
              @mouseleave="handleNumberInputBlur"
            >
              <button
                @click="handleValueChange(Math.max(uiState.displayValue - 1, 0))"
                :disabled="false"
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
                :disabled="false"
                class="w-8 h-8 flex items-center justify-center hover:bg-gray-200 rounded-r-lg transition-colors"
              >
                <ChevronUp class="w-3 h-3 text-content-color" />
              </button>
            </div>

            <!-- 필드별 에러 메시지 -->
            <FieldError :message="uiState.fieldErrors?.currentValue" />
          </div>
        </div>

        <!-- 타입 -->
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-lg flex items-center justify-center">
              <ListTodo class="w-5 h-5 text-content-color" />
            </div>
            <span class="text-gray-700 font-medium">타입</span>
          </div>
          <div class="text-gray-900 font-medium">{{ getTaskTypeLabel() }}</div>
        </div>

        <!-- 알림 -->
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-lg flex items-center justify-center">
              <Bell class="w-5 h-5 text-content-color" />
            </div>
            <span class="text-gray-700 font-medium">알림</span>
          </div>
          <div class="text-gray-900 font-medium">5분 전</div>
        </div>

        <!-- 반복(매주) -->
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-lg flex items-center justify-center">
              <RotateCcw class="w-5 h-5 text-content-color" />
            </div>
            <span class="text-gray-700 font-medium">반복(매주)</span>
          </div>
          <div class="flex items-center gap-2 text-sm">
            <span
              v-for="(day, index) in weekdayDisplay"
              :key="index"
              :class="[
                day.isEnabled
                  ? 'bg-[#6667D0] text-white'
                  : 'bg-gray-100 text-gray-700',
                'w-6 h-6 rounded-full flex items-center justify-center text-sm font-semibold'
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
            <span class="text-gray-700 font-medium">시간</span>
          </div>
          <div class="text-gray-900 font-medium">{{ dailyTask.time }}</div>
        </div>

        <!-- 메모 -->
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-lg flex items-center justify-center">
              <FileText class="w-5 h-5 text-content-color" />
            </div>
            <span class="text-gray-700 font-medium">메모</span>
          </div>
          <div class="text-gray-900 font-medium text-right max-w-xs">
            <p class="whitespace-pre-line break-words">{{ dailyTask.memo || '메모가 없습니다.' }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { ChevronDown, ChevronUp, Pencil, BookOpenCheck, Box, ListTodo, Bell, RotateCcw, Clock, FileText, TriangleAlert} from 'lucide-vue-next'
import { wrapApi } from '@/shared/utils/errorUtils.js'
import FieldError from '@/components/common/FieldError.vue'

// 더미 데이터
const dailyTask = ref({
  id: null,
  routineTaskId: 14,
  title: "달리기 30분 이상하기",
  date: "2025-07-26",
  taskType: "CHECK", // "CHECK", "COUNT", "MINUTES"
  currentValue: 0, // 서버의 실제 값
  time: "오전 7:00",
  alarmOffsetMinutes: 5,
  byDays: ['MO', 'TU', 'WE', 'TH', 'FR'],
  memo: "입력된값"
})

// UI 상태 관리 (단일 reactive 객체로 통합)
const uiState = reactive({
  displayValue: dailyTask.value.currentValue, // 화면에 표시되는 값
  isProcessing: false, // 서버 요청 진행 중
  errorMessage: '', // 전역 에러 메시지
  fieldErrors: {}, // 필드별 에러 메시지
  lastSavedValue: dailyTask.value.currentValue, // 마지막 저장 성공한 값
  syncStatus: 'synced' // 'synced', 'pending', 'failed'
})

// 단위 라벨
const getUnitLabel = () => {
  switch (dailyTask.value.taskType) {
    case 'COUNT': return '회'
    case 'MINUTES': return '분'
    default: return ''
  }
}

// 타입 라벨
const getTaskTypeLabel = () => {
  switch (dailyTask.value.taskType) {
    case 'CHECK': return '체크'
    case 'COUNT': return '횟수'
    case 'MINUTES': return '시간'
    default: return '알 수 없음'
  }
}

// 에러 메시지 제거
const clearError = () => {
  uiState.errorMessage = ''
  uiState.fieldErrors = {}
}

// API 호출 함수 (wrapApi로 감싸기)
const updateTaskProgressApi = wrapApi(
  async (requestData) => {
    const response = await fetch('/api/v1/daily-task/progress', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requestData)
    })

    if (!response.ok) {
      const error = new Error(`HTTP ${response.status}`)
      error.response = { data: await response.json() }
      throw error
    }

    return await response.json()
  },
  {
    fallbackMessage: '저장에 실패했습니다. 다시 시도해주세요.'
  }
)

// 옵티미스틱 업데이트 처리
const handleOptimisticUpdate = async (newValue) => {
  // 1. 즉시 UI 업데이트 (옵티미스틱)
  uiState.displayValue = newValue
  uiState.syncStatus = 'pending'
  uiState.errorMessage = ''
  uiState.fieldErrors = {}

  try {
    // 2. 서버에 비동기 요청
    const requestData = {
      id: dailyTask.value.id,
      routineTaskId: dailyTask.value.routineTaskId,
      currentValue: newValue,
      taskDate: dailyTask.value.date
    }

    console.log('API 요청 데이터:', requestData)

    // 실제 API 호출 (wrapApi로 감싸진 함수 사용)
    // const result = await updateTaskProgressApi(requestData)

    // API 호출 시뮬레이션 (실제로는 위의 updateTaskProgressApi 사용)
    await new Promise((resolve, reject) => {
      setTimeout(() => {
        // 50% 확률로 실패 시뮬레이션
        if (Math.random() < 0.5) {
          // 서버 에러 응답 시뮬레이션
          const error = new Error('Network Error')
          error.response = {
            data: {
              message: '서버 연결이 불안정합니다.',
              errors: [
                { value: 'currentValue', message: '잘못된 값입니다.' }
              ]
            }
          }
          reject(error)
        } else {
          resolve({
            id: dailyTask.value.id || Math.floor(Math.random() * 1000000)
          })
        }
      }, 800)
    })

    // 3. 성공 시 상태 업데이트
    // 시뮬레이션용 응답 (실제로는 updateTaskProgressApi 결과 사용)
    const result = {
      id: dailyTask.value.id || Math.floor(Math.random() * 1000000)
    }

    dailyTask.value.id = result.id
    dailyTask.value.currentValue = newValue
    uiState.lastSavedValue = newValue
    uiState.syncStatus = 'synced'

    console.log('저장 완료:', result)

  } catch (error) {
    // 4. 실패 시 롤백 및 에러 표시 (wrapApi가 처리한 에러)
    uiState.displayValue = uiState.lastSavedValue
    uiState.syncStatus = 'failed'

    // 필드별 에러가 있으면 필드 에러로, 없으면 전역 에러로 표시
    if (error.fieldErrors) {
      uiState.fieldErrors = error.fieldErrors
    } else {
      uiState.errorMessage = error.message
    }

    console.error('저장 실패:', error)
  }
}

// 체크박스 변경 처리 (즉시 UI 반영, 저장은 지연)
const handleCheckboxChange = (event) => {
  // 저장 중이면 무시
  if (uiState.syncStatus === 'pending') return

  const newValue = event.target.checked ? 1 : 0
  console.log('체크박스 클릭 - UI만 즉시 반영:', newValue)

  // 즉시 UI만 반영 (저장은 안함)
  uiState.displayValue = newValue

  // 값이 변경되었음을 표시
  if (newValue !== uiState.lastSavedValue) {
    uiState.syncStatus = 'pending'
  } else {
    uiState.syncStatus = 'synced'
  }
}

// 체크박스 영역에서 마우스가 벗어날 때 저장
const handleCheckboxBlur = () => {
  console.log('체크박스 마우스 아웃 - 저장 시도:', uiState.displayValue, 'vs', uiState.lastSavedValue)

  if (uiState.displayValue !== uiState.lastSavedValue && uiState.syncStatus === 'pending') {
    console.log('체크박스 저장 실행!')
    handleOptimisticUpdate(uiState.displayValue)
  }
}

// 숫자 값 변경 처리 (즉시 UI 반영, 저장은 지연)
const handleValueChange = (newValue) => {
  if (newValue < 0 || newValue > 999) return

  uiState.displayValue = newValue
  // 값이 변경되었음을 표시
  if (newValue !== uiState.lastSavedValue) {
    uiState.syncStatus = 'pending'
  } else {
    uiState.syncStatus = 'synced'
  }
}

// 숫자 입력 영역에서 마우스가 벗어날 때 저장
const handleNumberInputBlur = () => {
  if (uiState.displayValue !== uiState.lastSavedValue && uiState.syncStatus === 'pending') {
    handleOptimisticUpdate(uiState.displayValue)
  }
}

// 날짜 포맷팅
const formattedDateHeader = computed(() => {
  const date = new Date(dailyTask.value.date)
  const today = new Date()

  const isToday = date.toDateString() === today.toDateString()
  const weekdays = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일']
  const weekday = weekdays[date.getDay()]
  const month = date.getMonth() + 1
  const day = date.getDate()

  if (isToday) {
    return `오늘, ${month}월 ${day}일 ${weekday}`
  }

  const tomorrow = new Date(today)
  tomorrow.setDate(today.getDate() + 1)
  const isTomorrow = date.toDateString() === tomorrow.toDateString()

  if (isTomorrow) {
    return `내일, ${month}월 ${day}일 ${weekday}`
  }

  const yesterday = new Date(today)
  yesterday.setDate(today.getDate() - 1)
  const isYesterday = date.toDateString() === yesterday.toDateString()

  if (isYesterday) {
    return `어제, ${month}월 ${day}일 ${weekday}`
  }

  return `${month}월 ${day}일 ${weekday}`
})

// 반복 요일 표시 계산
const weekdayDisplay = computed(() => {
  const weekdayMap = {
    'MO': '월',
    'TU': '화',
    'WE': '수',
    'TH': '목',
    'FR': '금',
    'SA': '토',
    'SU': '일'
  }

  return Object.entries(weekdayMap).map(([code, label]) => ({
    label,
    isEnabled: dailyTask.value.byDays.includes(code)
  }))
})
</script>
