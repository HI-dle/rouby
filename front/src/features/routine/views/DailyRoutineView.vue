<script setup>
import { ref, watch } from 'vue'
import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'
import RoutineCard from '@/features/routine/components/RoutineCard.vue'

const selectedDate = ref(new Date())
const routines = ref([])

//임시 더미 함수로 대체
const fetchRoutinesByDate = async (date) => {
  const day = new Date(date).getDate() // 날짜만 추출

  if (day % 2 === 0) {
    // 짝수 날짜
    return [
      {
        id: 1,
        routineTaskId: 201,
        title: '아침 운동',
        time: '07:00',
        currentValue: 0,
        targetValue: 90,
        type: 'MINUTES',
      },
      {
        id: 2,
        routineTaskId: 202,
        title: '저녁 명상',
        time: '21:00',
        currentValue: 1,
        targetValue: 1,
        type: 'CHECK',
      },
    ]
  } else {
    // 홀수 날짜
    return [
      {
        id: 3,
        routineTaskId: 301,
        title: '점심 비타민',
        time: '13:00',
        currentValue: 0,
        targetValue: 1,
        type: 'CHECK',
      },
      {
        id: 4,
        routineTaskId: 302,
        title: '물 5컵 마시기',
        time: '10:00',
        currentValue: 3,
        targetValue: 5,
        type: 'COUNT',
      },
    ]
  }
}



const loadRoutines = async () => {
  routines.value = await fetchRoutinesByDate(selectedDate.value)
}

watch(selectedDate, loadRoutines, { immediate: true })
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !pt-0 !px-0">
      <WeeklyMonthlyDatePicker v-model="selectedDate" />
      <div class="mt-6 space-y-4 px-4">
        <RoutineCard
          v-for="routine in routines"
          :key="routine.id"
          :routine="routine"
          :date="selectedDate"
        />
      </div>
    </div>
  </div>
</template>
