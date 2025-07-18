<template>
  <div class="text-center mt-20 space-y-6 text-main-color">
    <div>
      <p class="text-base">{{ userName }}님은 {{ startTime }}에 하루를 시작하시는군요!</p>
      <p class="text-base">그렇다면, 언제 하루를 마감 하시나요?</p>
      <p class="text-sm text-[#6667D07A] mt-2">루비와 함께 하루를 마무리해요</p>
    </div>

    <div class="flex justify-center gap-4 mt-6">
      <SelectBox
        v-model="form.period"
        :options="periodOptions"
        placeholder="오전/오후"
        trigger-class="w-20 bg-white"
        content-class="w-20 min-w-20"
        item-class="w-16 justify-center px-1"
        :show-check="false"
      />
      <SelectBox
        v-model="form.hour"
        :options="hourOptions"
        placeholder="시간 선택"
        trigger-class="w-20 bg-white"
        content-class="w-20 min-w-20"
        item-class="w-16 justify-center px-1"
        :show-check="false"
      />
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import SelectBox from '@/components/common/SelectBox.vue'
import { useDateSettingForm } from '@/features/onboard/useDateSettingForm.js'
import { useOnboardStore } from '@/features/onboard/store/useOnboardStore.js'

const store = useOnboardStore()
const timeError = ref('')

const props = defineProps({
  userName: String,
  startDayTime: String,
})

const {
  form,
  periodOptions,
  hourOptions,
  selectedTime,
} = useDateSettingForm()

const userName = computed(() =>
  typeof props.userName === 'object' && 'value' in props.userName
    ? props.userName.value
    : props.userName
)

const startTime = computed(() =>
  typeof props.startDayTime === 'object' && 'value' in props.startDayTime
    ? props.startDayTime.value
    : props.startDayTime
)
const onNextClick = () => {
  timeError.value = ''

  if (!form.value.hour || !form.value.period) {
    timeError.value = '시간을 선택해주세요!'
    return false
  }

  // 시간 저장
  store.endOfDayTime = selectedTime.value

  return true
}

defineExpose({ onNextClick })
</script>
