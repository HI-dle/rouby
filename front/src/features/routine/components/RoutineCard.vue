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

          <div class="text-sm font-semibold text-gray-800 w-6 text-center">{{ countValue }}</div>

          <button
            @click="increment"
            class="p-1 rounded hover:bg-indigo-100"
          >
            <img src="@/assets/chevron-up.svg" alt="Increase" class="w-4 h-4 text-main-color" />
          </button>
        </div>
      </template>

      <!-- 시간 분 루틴 -->
      <template v-else-if="routine.type === 'MINUTES'">
        <div class="flex items-center space-x-6">
          <!-- 시간 -->
          <div class="flex flex-col items-center space-y-1">
            <button @click="increaseHour" class="p-1 rounded hover:bg-indigo-100">
              <img src="@/assets/chevron-up.svg" alt="Up" class="w-4 h-4 text-main-color" />
            </button>
            <div class="text-sm font-semibold text-gray-800 w-[56px] text-center">
              {{ hour }}시간
            </div>
            <button
              @click="decreaseHour"
              :disabled="currentValue < 60"
              class="p-1 rounded hover:bg-indigo-100 disabled:opacity-30"
            >
              <img src="@/assets/chevron-down.svg" alt="Down" class="w-4 h-4 text-main-color" />
            </button>
          </div>

          <!-- 분 -->
          <div class="flex flex-col items-center space-y-1">
            <button @click="increaseMinute" class="p-1 rounded hover:bg-indigo-100">
              <img src="@/assets/chevron-up.svg" alt="Up" class="w-4 h-4 text-main-color" />
            </button>
            <div class="text-sm font-semibold text-gray-800 w-[56px] text-center">
              {{ minute }}분
            </div>
            <button
              @click="decreaseMinute"
              :disabled="currentValue < minuteStep"
              class="p-1 rounded hover:bg-indigo-100 disabled:opacity-30"
            >
              <img src="@/assets/chevron-down.svg" alt="Down" class="w-4 h-4 text-main-color" />
            </button>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'

const props = defineProps({
  routine: {
    type: Object,
    required: true,
  },
})

const isChecked = ref(props.routine.currentValue >= 1)
const countValue = ref(props.routine.currentValue ?? 0)
const maxCount = props.routine.targetValue ?? 50
const minuteStep = 1

const currentValue = ref(props.routine.currentValue ?? 0)

const hour = computed(() => Math.floor(currentValue.value / 60))
const minute = computed(() => currentValue.value % 60)

watch(isChecked, (val) => {
  console.log(`${props.routine.title} 완료 여부:`, val)
})

watch(countValue, (val) => {
  console.log(`${props.routine.title} 카운트 값:`, val)
})

watch(currentValue, (val) => {
  console.log(`${props.routine.title} 시간 값(분):`, val)
})

// COUNT용
const increment = () => {
  countValue.value++
}
const decrement = () => {
  if (countValue.value > 0) countValue.value--
}

// MINUTES용 – maxCount 제한 제거
const increaseHour = () => {
  currentValue.value += 60
}
const decreaseHour = () => {
  if (currentValue.value >= 60) currentValue.value -= 60
}
const increaseMinute = () => {
  currentValue.value += minuteStep
}
const decreaseMinute = () => {
  if (currentValue.value >= minuteStep) currentValue.value -= minuteStep
}
</script>

<style scoped>
input[type="checkbox"] {
  accent-color: #6366f1;
}
</style>
