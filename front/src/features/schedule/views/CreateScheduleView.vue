<script setup>
import ScheduleForm from '../components/ScheduleForm.vue'
import { useScheduleForm } from '../useScheduleForm'

defineOptions({
  name: 'CreateScheduleView',
})

const { form, errors, memoRef, onSubmit, onDateTimeInput, autoResize } = useScheduleForm()
</script>

<template>
  <ScheduleForm
    v-model:form="form"
    :errors="errors"
    :memoRef="memoRef"
    @inputDatetime="onDateTimeInput"
    @submit="onSubmit"
    @autoResize="autoResize"
  />
</template>

<style scoped></style>
