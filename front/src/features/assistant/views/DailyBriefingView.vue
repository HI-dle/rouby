<script setup>
import WeeklyMonthlyDatePicker from '@/components/common/date-picker/WeeklyMonthlyDatePicker.vue'
import { useBriefingForm } from '@/features/assistant/useBriefingForm.js'
import BriefingForm from '@/features/assistant/component/BriefingForm.vue'

const props = defineProps({ date: { type: Number, required: true } })

const { selectedDate, form, loading, error,} = useBriefingForm(props.date)

</script>

<template>
  <div class="main-container">
    <div class="sub-main-container !pt-0 !px-0">
      <div class="sticky top-0 bg-white z-10">
        <WeeklyMonthlyDatePicker v-model="selectedDate" />
      </div>
      <BriefingForm
        :loading="loading"
        :error="error"
        :content="form.content"
      />
    </div>
  </div>
</template>
