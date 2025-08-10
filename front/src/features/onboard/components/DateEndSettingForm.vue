<template>
  <div class="text-center space-y-6 text-main-color">
    <div>
      <p class="text-base">
        {{ userName }}님은 {{ startDayTime }}에 하루를 시작하시는군요!
      </p>
      <p class="text-base">그렇다면, 언제 하루를 마감 하시나요?</p>
      <p class="text-sm text-[#6667D07A] mt-2">루비와 함께 하루를 마무리해요</p>
    </div>

    <div class="flex justify-center gap-4 mt-6">
      <SelectBox
        v-model="period"
        :options="periodOptions"
        placeholder="오전/오후"
        trigger-class="w-20 bg-white"
        content-class="w-20 min-w-20"
        item-class="w-16 justify-center px-1"
        :show-check="false"
      />
      <SelectBox
        v-model="hour"
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
import SelectBox from '@/components/common/SelectBox.vue'

const props = defineProps({
  userName: String,
  startDayTime: String,
  periodOptions: Array,
  hourOptions: Array,
})
const period = defineModel('period')
const hour = defineModel('hour')
</script>
