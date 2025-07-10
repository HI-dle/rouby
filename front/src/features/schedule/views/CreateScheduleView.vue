<script setup>
import { useGoBack } from '@/shared/composable/useGoBack'
import ScheduleForm from '../components/ScheduleForm.vue'
import { createSchedule } from '../scheduleService'
import { useScheduleForm } from '../useScheduleForm'
import { validateForm } from '../validations'

defineOptions({
  name: 'CreateScheduleView',
})

const { form, errors, onDateTimeInput, autoResize } = useScheduleForm()
const { goBackOrPath } = useGoBack()

const onSubmit = async () => {
  if (!validateForm(form, errors)) return
  try {
    const scheduleId = await createSchedule(form)
    goBackOrPath(`/schedule/detail/${scheduleId}`)
  } catch (err) {
    const msg = err.response?.data?.message || err.message || '저장 실패'
    alert(`저장에 실패하였습니다.\n${msg}`)
  }
}
const onCancel = async () => {
  goBackOrPath()
}
</script>

<template>
  <ScheduleForm
    v-model:form="form"
    :errors="errors"
    @inputDatetime="onDateTimeInput"
    @submit="onSubmit"
    @cancel="onCancel"
    @autoResize="autoResize"
  />
</template>

<style scoped></style>
