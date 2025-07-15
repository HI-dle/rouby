<script setup>
import SelectBox from "@/components/common/SelectBox.vue";
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps({
  form: Object,
  errors: Object,
  inputRefs: Object,
  errorModal: Object,
})


const emit = defineEmits(['submit', 'cancel', 'autoResize', 'inputDatetime'])

const days = ['월', '화', '수', '목', '금', '토', '일']

const toggleDay = (index) => {
  const exists = props.form.byDays.includes(index)
  if (exists) {
    props.form.byDays = props.form.byDays.filter(i => i !== index)
  } else {
    props.form.byDays.push(index)
  }
}

const typeOptions = [
  { value: 'CHECK', label: '체크 박스' },
  { value: 'MINUTES', label: '시간' },
  { value: 'COUNT', label: '횟수' },
]

const alarmOptions = [
  { value: 'NONE', label: '없음' },
  { value: 5, label: '5분 전' },
  { value: 10, label: '10분 전' },
  { value: 15, label: '15분 전' },
  { value: 30, label: '30분 전' },
  { value: 60, label: '1시간 전' },
  { value: 120, label: '2시간 전' },
  { value: 1440, label: '1일 전' },
  { value: 2880, label: '2일 전' },
  { value: 10080, label: '1주 전' },
]

</script>

<template>
  <div class="form-wrapper">
    <form @submit.prevent="$emit('submit')" class="space-y-6 p-6">
      <!-- 제목 -->
      <div>
      <input
        v-model="form.title"
        type="text"
        placeholder="제목"
        class="w-full h-10 font-medium bg-transparent px-2 shadow-[0_2px_0_0_rgba(0,0,0,0.05)] pb-2 mt-4 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)]"
      />
      <!-- <p v-if="errors.title" class="text-sm text-error-color mt-1 mx-2">{{ errors.title }}</p> -->
    </div>

      <!-- 시작 / 종료 -->
      <div class="space-y-2 w-full">
        <div class="grid grid-cols-[1fr_4fr] gap-4">
          <label class="flex h-10 items-center text-gray-700 px-2 font-medium mb-1">시작</label>
          <input
            type="date"
            v-model="form.startDate"
            @focus="showDatePicker = true"
            class="w-full text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
          />
        </div>
        <div class="grid grid-cols-[1fr_4fr] gap-4">
          <label class="flex h-10 items-center text-gray-700 px-2 font-medium mb-1">종료</label>
          <input
            type="datetime-local"
            v-model="form.until"
            @focus="showDatePicker = true"
            class="w-full text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
          />
        </div>
      </div>

      <!-- 시간 -->
      <div class="grid grid-cols-[1fr_4fr] gap-4">
        <label class="flex h-10 items-center  text-gray-700 px-2 font-medium mb-1">시간</label>
        <input
          type="time"
          v-model="form.time"
          class="w-full text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
        />
      </div>

      <!-- 타입 -->
      <div class="grid grid-cols-[1fr_4fr] gap-4">
        <label class="flex h-10 items-center  text-gray-700 px-2 font-medium mb-1">타입</label>
        <SelectBox
          v-model="form.taskType"
          :options="typeOptions"
          :placeholder="typeOptions[0].label"
          trigger-class="w-full bg-transparent border border-transparent"
        />
      </div>

      <!-- 목표값 -->
      <div class="grid grid-cols-[1fr_4fr] gap-4">
        <label class="flex h-10 items-center  text-gray-700 px-2 font-medium">목표값</label>
        <input
          v-model.number="form.targetValue"
          type="number"
          min="0"
          placeholder="숫자를 입력하세요"
          class="w-full text-base shadow-[0_2px_0_0_rgba(0,0,0,0.05)] px-2 pb-2 border-gray-300 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)] placeholder-gray-400 bg-inherit"
        />
      </div>

      <!-- 알림 -->
      <div class="grid grid-cols-[1fr_4fr] gap-4">
        <label class="flex h-10 items-center  text-gray-700 px-2 font-medium mb-1">알림</label>
        <SelectBox
          v-model="form.alarmOffsetMinutes"
          :options="alarmOptions"
          :placeholder="alarmOptions[0].label"
          trigger-class="w-full bg-transparent border border-transparent"
        />
      </div>

      <!-- 반복 요일 -->
      <div class="grid grid-cols-[1fr_4fr] gap-4">
        <label class="flex h-10 items-center  text-gray-700 px-2 font-medium">반복</label>
        <div class="flex gap-2 flex-wrap justify-between">
          <button
            v-for="(day, index) in days"
            :key="day"
            type="button"
            @click="toggleDay(index)"
            :class="[
                form.byDays.includes(index)
                  ? 'bg-[#6667D0] text-white'
                  : 'bg-gray-100 text-gray-700',
                'w-8 h-8 rounded-full flex items-center justify-center text-sm font-semibold'
              ]"
          >
            {{ day }}
          </button>
        </div>
      </div>

      <!-- 메모 -->
      <div class="grid grid-cols-[1fr_4fr] gap-4">
        <label class="flex h-10 items-center  text-gray-700 px-2 font-medium">메모</label>
        <textarea
          v-model="form.memo"
          class="w-full text-base shadow-[0_2px_0_0_rgba(0,0,0,0.05)] px-2 pb-2 border-gray-300 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)] placeholder-gray-400 resize-none overflow-auto bg-inherit"
          rows="1"
          @input="(e) => emit('autoResize', e)"
        />
      </div>

      <!-- 에러 -->
<!--      <div v-if="errorModal.show" class="text-red-500 text-sm">-->
<!--        {{ errorModal.msg }}-->
<!--      </div>-->

      <!-- 전송 버튼 -->
      <div class="flex justify-between pt-10 gap-2">
        <BaseButton
          @click="emit('cancel')"
          class="bg-none bg-gray-200 !text-content-color hover:bg-gray-300"
        >취소
        </BaseButton>
        <BaseButton type="submit">저장</BaseButton>
      </div>
    </form>
  </div>
</template>

