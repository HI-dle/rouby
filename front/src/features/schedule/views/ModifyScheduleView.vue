<script setup>
import { nextTick, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useScheduleStore } from '@/stores/useScheduleStore'
import { useScheduleForm } from '../useScheduleForm'
import ScheduleForm from '../components/ScheduleForm.vue'

defineOptions({
  name: 'ModifyScheduleView',
})

const route = useRoute()
const router = useRouter()
const scheduleStore = useScheduleStore()
let schedule = null

const {
  form,
  isSubmitting,
  errors,
  inputRefs,
  errorModal,
  onDateTimeInput,
  onSubmitForModify,
  onDeleteOne,
  onDeleteAll,
  initializeForModify,
} = useScheduleForm()

function init() {
  const scheduleId = route.params.id
  const instanceDate = route.params.date
  const instanceKey = `${scheduleId}@${instanceDate}`
  schedule = scheduleStore.getScheduleInstanceByKey(instanceKey)
  if (!schedule) {
    router.replace('/not-found')
    return
  }
  initializeForModify(schedule)
}

// 최초 마운트 시
onMounted(() => {
  init()
})

// 라우트 변경 시 (내부에서 push 등)
watch(
  () => [route.params.id, route.params.date],
  () => {
    init()
  }
)

const handleSubmit = async () => {
  const instanceDate = route.params.date
  await onSubmitForModify(
    schedule,
    (id) => router.push(`/schedule/${id}/${instanceDate}`),
    (msg) => {
      errorModal.msg = msg
      errorModal.show = true
    }
  )
}

const handleDeleteOne = async (schedule) => {
  const instanceDate = route.params.date
  console.log(schedule)
  await onDeleteOne(
    { scheduleId: schedule.id, instanceDate: instanceDate, startAt: schedule.start, endAt: schedule.end },
    () => {
      router.push('/schedule/daily/list')
    },
    (msg) => {
      errorModal.msg = msg
      errorModal.show = true
    },
  )
}

const handleDeleteAll = async (schedule) => {
  await onDeleteAll(
    { scheduleId: schedule.id, fromAt: schedule.start },
    () => {
      router.push('/schedule/daily/list')
    },
    (msg) => {
      errorModal.msg = msg
      errorModal.show = true
    },
  )
}

const onCancel = () => {
  router.back()
}

const autoResize = (key) => {
  nextTick(() => {
    const input = inputRefs[key]
    if (input && input.scrollHeight) {
      input.style.height = 'auto'
      input.style.height = input.scrollHeight + 'px'
    }
  })
}
</script>

<template>
  <div>
  <ScheduleForm
    v-model:form="form"
    :isSubmitting="isSubmitting"
    :errors="errors"
    :inputRefs="inputRefs"
    :errorModal="errorModal"
    mode="edit"
    @deleteOne="handleDeleteOne"
    @deleteAll="handleDeleteAll"
    @inputDatetime="onDateTimeInput"
    @submit="handleSubmit"
    @cancel="onCancel"
    @autoResize="autoResize"
  />
  </div>
</template>
