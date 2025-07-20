<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { debounce } from 'lodash'
import { format } from 'date-fns'

import { useScheduleStore } from '@/stores/uaeScheduleStore'
import { getMonthRange } from '@/shared/utils/dateUtils'
import { formatKoreanDatetime } from '@/shared/utils/dateTimeUtils'

import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'
import { getSchedules } from '../scheduleService'

const router = useRouter()

const selectedDate = ref(null)
const errorModal = reactive({})

const weekRange = computed(() => {
  if (!selectedDate.value)
    return { startOfThisMonth: null, startOfNextMonth: null }
  return getMonthRange(selectedDate.value, 0)
})

const startOfThisMonth = computed(() => weekRange.value.startOfThisMonth)
const startOfNextMonth = computed(() => weekRange.value.startOfNextMonth)

const scheduleStore = useScheduleStore()

const debouncedFetch = debounce(async (startAt, endAt) => {
  const { data } = await getSchedules(startAt, endAt)

  const key = format(startAt, 'yyyy-MM')
  scheduleStore.setMonthlySchedules(key, data?.schedules)
}, 300)

const fetchSchedulesByPeriod = (startAt, endAt) => {
  const key = format(startAt, 'yyyy-MM')
  if (scheduleStore.getMonthlySchedules(key)) return

  debouncedFetch(startAt, endAt)
}

const schedulesForSelectedDate = computed(() => {
  if (!selectedDate.value) return []
  return scheduleStore.getSchedulesForDate(selectedDate.value)
})

const goToScheduleDetail = (schedule) => {
  if (!schedule.id || !schedule.instanceDate) return

  router.push({
    name: 'ScheduleDetail',
    params: {
      id: schedule.id,
      date: schedule.instanceDate,
    },
  })
}

watch([startOfThisMonth, startOfNextMonth], ([s, e]) => {
  if (s && e) {
    fetchSchedulesByPeriod(s, e)
  }
})
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !pt-0 !px-0">
      <WeeklyMonthlyDatePicker v-model="selectedDate" />
      <div class="flex flex-col pt-4 px-4 gap-2 text-content-color text-base">
        <div
          v-for="schedule in schedulesForSelectedDate"
          @click="goToScheduleDetail(schedule)"
          :key="schedule.instanceKey"
          class="flex justify-between py-2 px-8 bg-white border border-border-color rounded-sm"
        >
          <div class="flex flex-col">
            <div class="font-semibold">{{ schedule.title }}</div>
            <div class="text-sm text-gray-500">
              {{ formatKoreanDatetime(schedule.startAt) }} ~
              {{ formatKoreanDatetime(schedule.endAt) }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- <BaseModal
    v-model="errorModal.show"
    :message="errorModal.msg"
    buttonText="확인"
  /> -->
</template>
