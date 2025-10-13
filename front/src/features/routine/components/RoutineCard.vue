<template>
  <div class="flex justify-between items-center bg-white rounded-xl p-4 shadow-sm border">
    <div class="flex flex-col">
      <span class="text-base font-semibold text-gray-700">{{ routine.title }}</span>
      <span class="text-xs text-gray-400 mt-2">{{ routine.time }}</span>
    </div>

    <div class="flex flex-col items-end">
      <!-- 체크박스 루틴 -->
      <template v-if="routine.type === 'CHECK'">
        <input
          type="checkbox"
          v-model="isChecked"
          @change="updateCheck"
          class="w-5 h-5 text-indigo-500 accent-indigo-500"
        />
      </template>

      <!-- 숫자 카운트 루틴 -->
      <template v-else-if="routine.type === 'COUNT'">
        <div class="flex items-center space-x-2">
          <button
            @click="decrement"
            :disabled="countValue <= 0"
            class="p-1 rounded hover:bg-indigo-100 disabled:opacity-30"
          >
            <img src="@/assets/chevron-down.svg" alt="Decrease" class="w-4 h-4 text-main-color" />
          </button>

          <input
            type="number"
            v-model.number="countValue"
            :min="0"
            :max="maxCount"
            class="text-sm font-semibold text-gray-800 w-8 text-center border rounded appearance-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none [&::-moz-appearance:textfield]"
          />

          <button
            @click="increment"
            :disabled="countValue >= maxCount"
            class="p-1 rounded hover:bg-indigo-100"
          >
            <img src="@/assets/chevron-up.svg" alt="Increase" class="w-4 h-4" />
          </button>
        </div>
      </template>

      <!-- 시간 분 루틴 -->
      <template v-else-if="routine.type === 'MINUTES'">
        <div class="flex items-center space-x-2">
          <!-- 시간 -->
          <div class="flex flex-col items-center">
            <button
              @click="increaseHour"
              class="flex justify-center items-center w-6 h-6 rounded hover:bg-indigo-100 mb-1"
            >
              <img src="@/assets/chevron-up.svg" alt="Up" class="w-4 h-4 text-main-color" />
            </button>
            <input
              type="number"
              v-model.number="hour"
              min="0"
              class="text-sm font-semibold text-gray-800 w-8 text-center border rounded appearance-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none [&::-moz-appearance:textfield]"
            />
            <button
              @click="decreaseHour"
              :disabled="currentValue < 60"
              class="flex justify-center items-center w-6 h-6 rounded hover:bg-indigo-100 mt-1 disabled:opacity-30"
            >
              <img src="@/assets/chevron-down.svg" alt="Down" class="w-4 h-4 text-main-color" />
            </button>
          </div>

          <span class="text-sm text-gray-700 mt-1 font-medium text-gray-800">시간</span>

          <!-- 분-->
          <div class="flex flex-col items-center ml-3">
            <button
              @click="increaseMinute"
              class="flex justify-center items-center w-6 h-6 rounded hover:bg-indigo-100 mb-1"
            >
              <img src="@/assets/chevron-up.svg" alt="Up" class="w-4 h-4 text-main-color" />
            </button>
            <input
              type="number"
              v-model.number="minute"
              min="0"
              max="59"
              class="text-sm font-semibold text-gray-800 w-8 text-center border rounded appearance-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none [&::-moz-appearance:textfield]"
            />
            <button
              @click="decreaseMinute"
              :disabled="currentValue < minuteStep"
              class="flex justify-center items-center w-6 h-6 rounded hover:bg-indigo-100 mt-1 disabled:opacity-30"
            >
              <img src="@/assets/chevron-down.svg" alt="Down" class="w-4 h-4 text-main-color" />
            </button>
          </div>

          <span class="text-sm text-gray-700 mt-1 font-medium text-gray-800">분</span>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useDailyTaskForm } from '@/features/routine-task/useDailyTaskForm'

const props = defineProps({
  routine: Object,
  date: Date,
})

const { handleCheckboxChange, handleValueChange } = useDailyTaskForm(props.routine)

// 체크박스
const isChecked = ref(props.routine.currentValue >= 1)

// COUNT
const maxCount = props.routine.targetValue ?? 50
const countValue = ref(props.routine.currentValue ?? 0)

// MINUTES
const currentValue = ref(props.routine.currentValue ?? 0)
const minuteStep = 1

// MINUTES input도 수정 가능하게 computed getter + setter
const hour = computed({
  get: () => Math.floor(currentValue.value / 60),
  set: (val) => {
    currentValue.value = val * 60 + (currentValue.value % 60)
  },
})

const minute = computed({
  get: () => currentValue.value % 60,
  set: (val) => {
    const h = Math.floor(currentValue.value / 60)
    currentValue.value = h * 60 + val
  },
})

// === Watch & API ===
watch(isChecked, (val, old) => {
  if (val === old) return
  handleCheckboxChange({ target: { checked: val } })
})

watch(countValue, (val, old) => {
  if (val === old) return
  handleValueChange(val)
})

watch(currentValue, (val, old) => {
  if (val === old) return
  handleValueChange(val)
})

// === UI Actions ===
const increment = () => {
  if (countValue.value < maxCount) countValue.value++
}
const decrement = () => {
  if (countValue.value > 0) countValue.value--
}
const increaseHour = () => (currentValue.value += 60)
const decreaseHour = () => {
  if (currentValue.value >= 60) currentValue.value -= 60
}
const increaseMinute = () => (currentValue.value += minuteStep)
const decreaseMinute = () => {
  if (currentValue.value >= minuteStep) currentValue.value -= minuteStep
}
</script>

<style scoped>
input[type="checkbox"] {
  accent-color: #6366f1;
}
</style>
