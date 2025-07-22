<script setup>
import { computed } from 'vue'

import { useGoBack } from '@/shared/composable/useGoBack'
import { useAutoResize } from '@/shared/composable/useAutoResize'
import { useScheduleForm } from '../useScheduleForm'
import ScheduleForm from '../components/ScheduleForm.vue'
import { useScheduleStore } from '@/stores/uaeScheduleStore'

const props = defineProps({
  id: Number, // 또는 Number, 형식에 맞게
  date: String,
})
const instanceKey = `${props.id}@${props.date}`

const scheduleStore = useScheduleStore()
const scheduleInstance = computed(() => {
  return scheduleStore.getScheduleInstanceByKey(instanceKey.value)
})

defineOptions({
  name: 'ModifyScheduleView',
})

const {
  form,
  isSubmitting,
  errors,
  inputRefs,
  errorModal,
  onDateTimeInput,
  onSubmit,
} = useScheduleForm()
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
