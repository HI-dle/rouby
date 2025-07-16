<script setup>
import SelectBox from "@/components/common/SelectBox.vue";
import BaseButton from '@/components/common/BaseButton.vue'
import { days, typeOptions, alarmOptions } from '../constants'
import { Calendar, Bell, ListTodo, StickyNote } from 'lucide-vue-next'

const props = defineProps({
  form: Object,
  errors: Object,
  inputRefs: Object,
  errorModal: Object,
})

const emit = defineEmits(['submit', 'cancel', 'autoResize', 'inputDatetime'])

const toggleDay = (index) => {
  const exists = props.form.byDays.includes(index)
  if (exists) {
    props.form.byDays = props.form.byDays.filter(i => i !== index)
  } else {
    props.form.byDays.push(index)
  }
}

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
      <div class="w-full flex justify-end">
        <!-- 오른쪽 영역: 아이콘 + 폼 컨테이너 -->
        <div class="flex w-full max-w-4xl gap-4">
          <!-- 아이콘 -->
          <div class="flex items-start justify-center pt-2">
            <Calendar class="w-4 h-4 text-content-color" />
          </div>

          <!-- 폼 영역 -->
          <div class="flex flex-col gap-4 flex-1">
            <!-- 요일 반복 선택 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">반복</label>
              <div class="flex flex-wrap gap-2 justify-between flex-[9_9_0%]">
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

            <!-- 시작 날짜 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">시작</label>
              <input
                type="date"
                v-model="form.startDate"
                @focus="showDatePicker = true"
                class="flex-[9_9_0%] text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
              />
            </div>

            <!-- 종료 날짜 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">종료</label>
              <input
                type="date"
                v-model="form.until"
                @focus="showDatePicker = true"
                class="flex-[9_9_0%] text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
              />
            </div>

            <!-- 시간 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">시간</label>
              <input
                type="time"
                v-model="form.time"
                class="flex-[9_9_0%] text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="w-full flex justify-end">
        <!-- 오른쪽 영역: 아이콘 + 폼 컨테이너 -->
        <div class="flex w-full max-w-4xl gap-4">
          <!-- 아이콘 -->
          <div class="flex items-start justify-center pt-2">
            <ListTodo class="w-4 h-4 text-content-color" />
          </div>

          <!-- 폼 영역 (세로 배치) -->
          <div class="flex flex-col gap-4 flex-1">
            <!-- 타입 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">타입</label>
              <div class="flex-[9_9_0%]">
                <SelectBox
                  v-model="form.taskType"
                  :options="typeOptions"
                  :placeholder="typeOptions[0].label"
                  trigger-class="w-full bg-transparent border border-transparent"
                />
              </div>
            </div>

            <!-- 목표값 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">목표값</label>
              <input
                v-model.number="form.targetValue"
                type="number"
                min="0"
                placeholder="숫자를 입력하세요"
                class="flex-[9_9_0%] text-base shadow-[0_2px_0_0_rgba(0,0,0,0.05)] px-2 pb-2 border-gray-300 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)] placeholder-gray-400 bg-inherit"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 알림 -->
      <div class="w-full flex justify-end">
        <div class="flex w-full max-w-4xl gap-4">
          <div class="flex items-start justify-center pt-2">
            <Bell class="w-4 h-4 text-content-color" />
          </div>
          <div class="flex gap-4 flex-1">
            <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">알림</label>
            <div class="flex-[9_9_0%]">
              <SelectBox
                v-model="form.alarmOffsetMinutes"
                :options="alarmOptions"
                :placeholder="alarmOptions[0].label"
                trigger-class="w-full bg-transparent border border-transparent"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 메모 -->
      <div class="w-full flex justify-end">
        <div class="flex w-full max-w-4xl gap-4">
          <div class="flex items-start justify-center pt-2">
            <StickyNote class="w-4 h-4 text-content-color" />
          </div>
          <div class="flex gap-4 flex-1">
            <label class="flex items-center text-gray-700 font-medium flex-[1_1_0%]">메모</label>
            <textarea
              v-model="form.memo"
              class="flex-[9_9_0%] text-base shadow-[0_2px_0_0_rgba(0,0,0,0.05)] px-2 pb-2 border-gray-300 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)] placeholder-gray-400 resize-none overflow-auto bg-inherit"
              rows="1"
              @input="(e) => emit('autoResize', e)"
            />
          </div>
        </div>
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

