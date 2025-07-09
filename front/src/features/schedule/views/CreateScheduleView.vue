<script setup>
import { nextTick, onMounted, watch } from 'vue'
import ScheduleForm from '../components/ScheduleForm.vue'
import { useScheduleForm } from '../useScheduleForm'

defineOptions({
  name: 'CreateScheduleView',
})

const { form, errors, memoRef, onSubmit, onDateTimeInput } = useScheduleForm()
</script>

<template>
  <ScheduleForm
    v-model:form="form"
    :errors="errors"
    :memoRef="memoRef"
    @input-datetime="onDateTimeInput"
    @submit="onSubmit"
    @autoResize="autoResize"
  />
</template>

<style scoped></style>
