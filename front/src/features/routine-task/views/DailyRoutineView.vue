<script setup>
import { ref, watch } from 'vue'
import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'
import RoutineCard from '@/features/routine-task/components/RoutineCard.vue'

import { useRoutineForm } from '@/features/routine-task/useRoutineForm'

const selectedDate = ref(new Date())

const { routinesForSelectedDate } = useRoutineForm(selectedDate)
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !pt-0 !px-0">
      <WeeklyMonthlyDatePicker v-model="selectedDate" />
      <div class="mt-6 space-y-4 px-4">
        <RoutineCard
          v-for="routine in routinesForSelectedDate"
          :key="routine.id"
          :routine="routine"
          :date="selectedDate"
        />
      </div>
    </div>
  </div>
</template>
