<script setup>
import { ref, watch } from 'vue'
import FullCalendar from '@fullcalendar/vue3'
import { ChevronLeft, ChevronRight } from 'lucide-vue-next'
import { useDatePickStore } from '@/stores/useDatePickStore'
import { useDatePickerSelectedDate } from '@/shared/composable/useDatePickerSelectedDate'
import { useDatePickerDates } from '@/shared/composable/useDatePickerDates'
import { useDatePickerState } from '@/shared/composable/useDatePickerState'
import { useScheduleList } from '@/features/schedule/useScheduleList'
import BaseButton from '@/components/common/BaseButton.vue'
import { useScheduleCalendar } from '../useScheduleCalendar'

const today = ref(new Date())
const isMonthly = ref(true)
const datePickStore = useDatePickStore()

const selectedDate = useDatePickerSelectedDate(today, null, datePickStore)
const baseDate = ref(selectedDate.value)

const { currentMonthLabel } = useDatePickerDates(baseDate, selectedDate, today)
const { handleBaseDateWatch } = useDatePickerState({
  baseDate,
  selectedDate,
  isMonthly,
  datePickStore,
})
watch(baseDate, handleBaseDateWatch, { immediate: true })

const { schedulesForSelectedMonth, goToScheduleDetailOf } =
  useScheduleList(selectedDate)

const { calendarRef, calendarOptions, prevMonth, nextMonth } =
  useScheduleCalendar(schedulesForSelectedMonth, baseDate, goToScheduleDetailOf)
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !px-0">
      <div class="">
        <div class="flex justify-center items-center gap-2 mb-2">
          <BaseButton
            @click="prevMonth"
            class="flex w-6 h-6 justify-center items-center text-base px-1 ml-6 bg-none bg-border-color text-main-color"
            aria-label="이전 월"
          >
            <ChevronLeft class="h-4 w-4" />
          </BaseButton>
          <h2
            class="px-4 text-lg sm:text-xl font-semibold text-center text-content-color"
          >
            {{ currentMonthLabel }}
          </h2>
          <BaseButton
            @click="nextMonth"
            class="flex w-6 h-6 justify-center items-center text-base px-1 mr-6 bg-none bg-border-color text-main-color"
            aria-label="다음 월"
          >
            <ChevronRight class="h-4 w-4" />
          </BaseButton>
        </div>
        <FullCalendar
          ref="calendarRef"
          :options="calendarOptions"
          class="custom-calendar"
        />
      </div>
    </div>
  </div>
</template>

<style src="@/styles/fullCalendarCustom.css"></style>
