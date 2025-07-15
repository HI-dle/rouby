<script setup>
import { ChevronLeft, ChevronRight } from 'lucide-vue-next'
import { isSameDay, isSameMonth } from 'date-fns'
import BaseButton from '@/components/common/BaseButton.vue'
import { useDatePickerDates } from '@/shared/composable/useDatePickerDates'
import { weekdays } from '@/shared/constants/date'
import { toRefs } from 'vue'

const props = defineProps({
  baseDate: Object,
  selectedDate: Object,
  today: Object,
  prevMonth: Function,
  nextMonth: Function,
  selectDate: Function,
})
const { baseDate, selectedDate, today } = toRefs(props)

const { calendarDays, isToday, isSelected } = useDatePickerDates(baseDate, selectedDate, today)
</script>
<template>
  <div class="flex justify-between items-center select-none">
    <BaseButton
      @click="prevMonth"
      class="hidden md:flex w-6 h-6 justify-center items-center text-base px-1 ml-6"
      aria-label="이전 주"
    >
      <ChevronLeft class="h-4 w-4" />
    </BaseButton>
    <div class="flex flex-col w-full px-2">
      <!-- 요일 헤더 -->
      <div class="grid grid-cols-7 text-center text-sm text-content-color mb-2">
        <div v-for="(day, index) in weekdays" :key="index">{{ day }}</div>
      </div>

      <!-- 날짜 그리드 -->
      <div class="grid grid-cols-7 gap-y-1">
        <div class="flex w-full justify-center" v-for="(date, index) in calendarDays" :key="index">
          <div
            :key="index"
            @click="() => selectDate?.(date)"
            class="w-8 h-8 rounded-xl flex items-center justify-center cursor-pointer"
            :class="{
              'md:hover:bg-gray-100': !isSelected(date),
              'bg-border-color text-black': isToday(date),
              'bg-gradient-to-r from-button-from to-button-to hover:from-[#5a63d8] hover:to-[#693f99] transition text-white':
                isSelected(date),
              'text-gray-400': !isSameMonth(date, baseDate),
            }"
          >
            {{ date.getDate() }}
          </div>
        </div>
      </div>
    </div>
    <BaseButton
      @click="nextMonth"
      class="hidden md:flex w-6 h-6 justify-center items-center text-xl px-1 mr-6"
      aria-label="다음 주"
    >
      <ChevronRight class="h-4 w-4" />
    </BaseButton>
  </div>
</template>
