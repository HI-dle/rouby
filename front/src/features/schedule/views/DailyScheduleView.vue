<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { format, differenceInCalendarDays, subDays } from 'date-fns'

import { useScheduleStore } from '@/stores/useScheduleStore'
import { getMonthRange } from '@/shared/utils/dateUtils'
import { formatKoreanDatetime } from '@/shared/utils/dateTimeUtils'

import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'
import { getSchedules } from '../scheduleService'
import { debounce } from 'lodash-es'
import BaseModal from '@/components/common/BaseModal.vue'
import { Bell, RefreshCw } from 'lucide-vue-next'

const router = useRouter()
const selectedDate = ref(null)
const errorModal = reactive({})
const weekRange = computed(() => {
  if (!selectedDate.value) return { rangeStart: null, rangeEnd: null }

  const { rangeStart, rangeEnd } = getMonthRange(selectedDate.value)
  return { rangeStart, rangeEnd }
})

const startOfThisMonth = computed(() => weekRange.value.rangeStart)
const startOfNextMonth = computed(() => weekRange.value.rangeEnd)

const scheduleStore = useScheduleStore()

const debouncedFetch = debounce(async (fromAt, toAt) => {
  try {
    const { data } = await getSchedules(fromAt, toAt)

    const key = format(fromAt, 'yyyy-MM')
    scheduleStore.setMonthlySchedules(key, data?.schedules)
  } catch (err) {
    console.error(err)
    const msg = err.response?.data?.message || err.message || '조회 실패'
    errorModal.show = true
    errorModal.msg = msg
  }
}, 300)

const fetchSchedulesByPeriod = (fromAt, toAt) => {
  const key = format(fromAt, 'yyyy-MM')
  if (scheduleStore.getMonthlySchedules(key)) return

  debouncedFetch(fromAt, toAt)
}

const schedulesForSelectedDate = computed(() => {
  if (!selectedDate.value) return []
  return scheduleStore.getSchedulesForDate(selectedDate.value)
})

const goToScheduleDetail = (schedule) => {
  if (!schedule.id || !schedule.instanceDate) return

  router.push({
    name: 'schedule-modify',
    params: {
      id: schedule.id,
      date: schedule.instanceDate,
    },
  })
}

function formatSchedulePeriod(startAt, endAt) {
  const start = new Date(startAt)
  const end = new Date(endAt)
  const endMinus = subDays(end, 1)

  const isAllDayStart = start.getHours() === 0 && start.getMinutes() === 0
  const isAllDayEnd = end.getHours() === 0 && end.getMinutes() === 0
  const isAllDay = isAllDayStart && isAllDayEnd

  const oneDay = differenceInCalendarDays(end, start) === 1

  if (isAllDay && oneDay) {
    return '하루 종일'
  }

  const dateFormat = 'yyyy년 M월 d일'
  const timeFormat = 'HH:mm'

  const formatDateTime = (date, includeTime) =>
    format(date, includeTime ? `${dateFormat} ${timeFormat}` : dateFormat)

  const formattedStart = formatDateTime(start, !isAllDay)
  const formattedEnd = formatDateTime(isAllDay ? endMinus : end, !isAllDay)

  return `${formattedStart} ~ ${formattedEnd}`
}

watch([startOfThisMonth, startOfNextMonth], ([s, n]) => {
  if (s && n) {
    fetchSchedulesByPeriod(s, n)
  }
})
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
                  v-if="schedule.recurrenceRule.rruleStr"
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
