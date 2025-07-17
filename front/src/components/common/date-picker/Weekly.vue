<script setup>
import { toRefs } from 'vue'
import { ChevronLeft, ChevronRight } from 'lucide-vue-next'
import { weekdays } from '@/shared/constants/date'
import BaseButton from '@/components/common/BaseButton.vue'
import { useDatePickerDates } from '@/shared/composable/useDatePickerDates'

const props = defineProps({
  baseDate: {
    type: Date,
    required: true,
  },
  selectedDate: {
    type: Date,
    required: true,
  },
  today: {
    type: Date,
    required: true,
  },
  prevWeek: {
    type: Function,
    required: true,
  },
  nextWeek: {
    type: Function,
    required: true,
  },
  selectDate: {
    type: Function,
    required: true,
  },
})

const { baseDate, selectedDate, today } = toRefs(props)
const { weekDates, isToday, isSelected } = useDatePickerDates(baseDate, selectedDate, today)

const getDateClasses = (date) => {
  return {
    'md:hover:bg-gray-100': !isSelected(date),
    'bg-border-color text-black': isToday(date),
    'bg-gradient-to-r from-button-from to-button-to hover:from-[#5a63d8] hover:to-[#693f99] transition text-white':
      isSelected(date),
  }
}
</script>

<template>
  <div class="flex justify-between items-center select-none">
    <BaseButton
      @click="prevWeek"
      class="hidden md:flex w-6 h-6 justify-center items-center text-base px-1 ml-6 bg-none bg-border-color text-main-color"
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
      <div class="grid grid-cols-7">
        <div class="flex w-full justify-center" v-for="(date, index) in weekDates" :key="index">
          <div
            @click="selectDate(date)"
            class="w-8 h-8 rounded-xl flex items-center justify-center cursor-pointer"
            :class="getDateClasses(date)"
          >
            {{ date.getDate() }}
          </div>
        </div>
      </div>
    </div>

    <BaseButton
      @click="nextWeek"
      class="hidden md:flex w-6 h-6 justify-center items-center text-base px-1 mr-6 bg-none bg-border-color text-main-color"
      aria-label="다음 주"
    >
      <ChevronRight class="h-4 w-4" />
    </BaseButton>
  </div>
</template>
