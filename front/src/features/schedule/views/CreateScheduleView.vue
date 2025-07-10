<script setup>
import { useGoBack } from '@/shared/composable/useGoBack'
import { useAutoResize } from '@/shared/composable/useAutoResize'
import { useScheduleForm } from '../useScheduleForm'
import ScheduleForm from '../components/ScheduleForm.vue'

defineOptions({
  name: 'CreateScheduleView',
})

const { form, errors, onDateTimeInput, onSubmit } = useScheduleForm()
const { goBackOrPath } = useGoBack()
const { autoResize } = useAutoResize()

const handleSubmit = () => {
  onSubmit(
    // 성공 콜백
    (scheduleId) => {
      goBackOrPath(`/schedule/detail/${scheduleId}`)
    },
    // 에러 콜백
    (errorMsg) => {
      alert(`저장에 실패하였습니다.\n${errorMsg}`)
    },
  )
}

const onCancel = () => goBackOrPath()
</script>

<template>
  <ScheduleForm
    v-model:form="form"
    :errors="errors"
    @inputDatetime="onDateTimeInput"
    @submit="handleSubmit"
    @cancel="onCancel"
    @autoResize="autoResize"
  />
</template>

<style scoped></style>
