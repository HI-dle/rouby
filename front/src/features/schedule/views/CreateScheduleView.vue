<script setup>
import { useGoBack } from '@/shared/composable/useGoBack'
import { useAutoResize } from '@/shared/composable/useAutoResize'
import { useScheduleForm } from '../useScheduleForm'
import ScheduleForm from '../components/ScheduleForm.vue'

defineOptions({
  name: 'CreateScheduleView',
})

const { form, isSubmitting, errors, inputRefs, errorModal, onDateTimeInput, onSubmit } =
  useScheduleForm()
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
      errorModal.msg = errorMsg
      errorModal.show = true
    },
  )
}

const onCancel = () => goBackOrPath()
</script>

<template>
  <ScheduleForm
    v-model:form="form"
    :isSubmitting="isSubmitting"
    :errors="errors"
    :inputRefs="inputRefs"
    :errorModal="errorModal"
    @inputDatetime="onDateTimeInput"
    @submit="handleSubmit"
    @cancel="onCancel"
    @autoResize="autoResize"
  />
</template>
