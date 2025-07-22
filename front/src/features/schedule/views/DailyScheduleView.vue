<script setup>
import { Bell, RefreshCw } from 'lucide-vue-next'
import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'
import BaseModal from '@/components/common/BaseModal.vue'
import { useScheduleList } from '@/features/schedule/useScheduleList'

const {
  selectedDate,
  errorModal,
  schedulesForSelectedDate,
  goToScheduleDetail,
  formatSchedulePeriod,
} = useScheduleList()
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !pt-0 !px-0">
      <WeeklyMonthlyDatePicker v-model="selectedDate" />
      <div class="flex flex-col pt-4 px-4 gap-3 text-content-color text-base">
        <div
          v-for="schedule in schedulesForSelectedDate"
          @click="goToScheduleDetail(schedule)"
          :key="schedule.instanceKey"
          class="flex justify-between py-4 px-8 bg-white shadow shadow-border-color rounded-xl"
        >
          <div class="flex flex-col w-full">
            <div class="font-semibold">{{ schedule.title }}</div>
            <div class="flex justify-between items-center mt-1">
              <div class="text-xs text-gray-500">
                {{ formatSchedulePeriod(schedule.startAt, schedule.endAt) }}
              </div>
              <div class="flex justify-normal">
                <Bell v-if="schedule.alarmOffsetMinutes" class="ml-2 h-2 w-2" />
                <RefreshCw
                  v-if="schedule.recurrenceRule?.rruleStr"
                  class="ml-2 h-2 w-2"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <BaseModal
    v-model="errorModal.show"
    :message="errorModal.msg"
    buttonText="확인"
  />
</template>
