<script setup>
import { useGoBack } from '@/shared/composable/useGoBack'
import { useAutoResize } from '@/shared/composable/useAutoResize'
import { useRoutineTaskForm } from '../useRoutineTaskForm'
import RoutineTaskForm from '../components/RoutineTaskForm.vue'

defineOptions({
  name: 'CreateRoutineTaskView',
})

const {
  form,
  errors,
  onSubmit,
  errorModal,
  inputRefs,
  onDateTimeInput,
} = useRoutineTaskForm()

const { autoResize } = useAutoResize()
const { goBackOrPath } = useGoBack()

const handleSubmit = () => {
  onSubmit(
    // success
    (id) => goBackOrPath(`/routine-task/detail/${id}`),
    // failure
    (msg) => {
      errorModal.msg = msg
      errorModal.show = true
    }
  )
}

const onCancel = () => goBackOrPath()

</script>

<template>
  <RoutineTaskForm
    v-model:form="form"
    :errors="errors"
    :inputRefs="inputRefs"
    :errorModal="errorModal"
    @submit="handleSubmit"
    @cancel="onCancel"
    @autoResize="autoResize"
    @inputDatetime="onDateTimeInput"
  />
</template>
