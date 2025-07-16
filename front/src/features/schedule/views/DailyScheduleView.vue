<script setup>
import { ref } from 'vue'
import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'

const selectedDate = ref(null)
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !pt-0 !px-0">
      <WeeklyMonthlyDatePicker v-model="selectedDate" />
      <div class="flex flex-col pt-4 px-4 gap-2 text-content-color text-base">
        <div
          class="flex justify-between py-2 px-8 border border-border-color bg-inherit rounded-sm"
        >
          <div>test</div>
          <div>25.12.12</div>
        </div>
        <div class="flex justify-between py-2 px-8 bg-white rounded-sm">
          <div>test</div>
          <div>25.12.12</div>
        </div>
        <p>선택된 날짜: {{ selectedDate }}</p>
      </div>
    </div>
  </div>
</template>
