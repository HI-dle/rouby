<script setup>
import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'
import Messages from '../component/Messages.vue'
import { useFeedbackList } from '../useFeedbackList'

const { selectedDate, messages, nomessages } = useFeedbackList()
</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !pt-0 !px-0">
      <WeeklyMonthlyDatePicker v-model="selectedDate" />
      <div class="px-8">
        <Messages
          v-if="messages.length > 0"
          :messages="messages"
          class="mt-7"
        />
        <p v-else class="text-content-color text-center mt-6">
          {{ nomessages }}
        </p>
      </div>
    </div>
  </div>
</template>
