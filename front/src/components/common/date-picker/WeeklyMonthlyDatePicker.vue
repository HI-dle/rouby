<script setup>
import { ref, computed, watch } from 'vue'
import { addMonths, addWeeks, format, subMonths, subWeeks } from 'date-fns'
import { ko } from 'date-fns/locale'
import { CalendarArrowDown, CalendarArrowUp } from 'lucide-vue-next'
import Weekly from './Weekly.vue'
import Monthly from './Monthly.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import { useDatePickStore } from '@/stores/useDatePickStore'
import { useDatePickerState } from '@/shared/composable/useDatePickerState'
import { useDatePickerGestures } from '@/shared/composable/useDatePickerGestures'
import { useDatePickerSelectedDate } from '@/shared/composable/useDatePickerSelectedDate'
import { useDatePickerDates } from '@/shared/composable/useDatePickerDates'

const today = new Date()
const isMonthly = ref(false)

const props = defineProps({
  modelValue: {
    type: Date,
  },
})
const emit = defineEmits(['update:modelValue'])

const datePickStore = useDatePickStore()
const selectedDate = useDatePickerSelectedDate(props, emit, datePickStore)
const baseDate = ref(selectedDate.value)

const { currentMonthLabel } = useDatePickerDates(baseDate, selectedDate, today)
const { selectDate, syncStoreOnWeeklyModeSwitch, handleBaseDateWatch } = useDatePickerState({
  baseDate,
  selectedDate,
  isMonthly,
  datePickStore,
})

watch(isMonthly, syncStoreOnWeeklyModeSwitch)
watch(baseDate, handleBaseDateWatch, { immediate: false })

const prevWeek = () => (baseDate.value = subWeeks(baseDate.value, 1))
const nextWeek = () => (baseDate.value = addWeeks(baseDate.value, 1))
const prevMonth = () => (baseDate.value = subMonths(baseDate.value, 1))
const nextMonth = () => (baseDate.value = addMonths(baseDate.value, 1))

const { onTouchStart, onTouchEnd } = useDatePickerGestures({
  isMonthly,
  prevWeek,
  nextWeek,
  prevMonth,
  nextMonth,
})
</script>

<template>
  <div @touchstart="onTouchStart" @touchend="onTouchEnd">
    <div class="flex justify-center items-center gap-2 mb-2">
      <BaseButton
        @click="() => (isMonthly = !isMonthly)"
        class="hidden md:flex w-6 h-6 justify-center items-center bg-none text-base text-main-color"
        :aria-label="isMonthly ? '주간 보기로 전환' : '월간 보기로 전환'"
        :title="isMonthly ? '주간 보기로 전환' : '월간 보기로 전환'"
      >
        <CalendarArrowUp v-if="isMonthly" class="h-4 w-4" />
        <CalendarArrowDown v-else class="h-4 w-4" />
      </BaseButton>
      <h2 class="text-lg sm:text-xl font-semibold text-center w-full sm:w-auto text-content-color">
        {{ currentMonthLabel }}
      </h2>
    </div>

    <Weekly
      v-if="!isMonthly"
      :base-date="baseDate"
      :selected-date="selectedDate"
      :today="today"
      :prev-week="prevWeek"
      :next-week="nextWeek"
      :select-date="selectDate"
    />

    <!-- 월 달력 -->
    <Monthly
      v-else
      :base-date="baseDate"
      :selected-date="selectedDate"
      :today="today"
      :prev-month="prevMonth"
      :next-month="nextMonth"
      :select-date="selectDate"
    />
  </div>
</template>
