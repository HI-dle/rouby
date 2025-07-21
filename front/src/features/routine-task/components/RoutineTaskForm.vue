<script setup>
import SelectBox from "@/components/common/SelectBox.vue";
import BaseButton from '@/components/common/BaseButton.vue'
import FieldError from '@/components/common/FieldError.vue'
import {alarmOptions, dayEnums, days, typeOptions} from '../constants'
import {Bell, Calendar, ListTodo, StickyNote} from 'lucide-vue-next'

const props = defineProps({
  form: Object,
  errors: Object,
  inputRefs: Object,
  errorModal: Object,
})

const emit = defineEmits(['submit', 'cancel', 'autoResize', 'inputDatetime'])

const toggleDay = (dayEnum) => {
  const exists = props.form.byDays.includes(dayEnum)
  if (exists) {
    props.form.byDays = props.form.byDays.filter((d) => d !== dayEnum)
  } else {
    props.form.byDays.push(dayEnum)
  }
}

const onTaskTypeChange = (value) => {
  props.form.taskType = value
  props.form.targetValue = 1
}

</script>

<template>
  <div class="main-container">
  <div class="sub-main-container">
    <form @submit.prevent="$emit('submit')" class="space-y-6">
      <!-- 제목 -->
      <div>
      <input
        v-model="form.title"
        type="text"
        placeholder="제목"
        class="w-full h-10 font-medium bg-transparent px-2 shadow-[0_2px_0_0_rgba(0,0,0,0.05)] pb-2 mt-4 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)]"
      />
      <FieldError :message="errors.title" />
    </div>
       <!--일정-->
      <div class="w-full flex justify-end">
        <!-- 오른쪽 영역: 아이콘 + 폼 컨테이너 -->
        <div class="flex w-full max-w-4xl gap-4">
          <!-- 아이콘 -->
          <div class="flex flex-col justify-center h-[31.99px] ">
            <Calendar class="w-4 h-4 text-content-color" />
          </div>

          <!-- 폼 영역 -->
          <div class="flex flex-col gap-4 flex-1">
            <!-- 요일 반복 선택 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">요일</label>
              <div class="flex flex-wrap gap-2 justify-between flex-[9_9_0%]">
                <button
                  v-for="(day, index) in days"
                  :key="dayEnums[index]"
                  type="button"
                  @click="toggleDay(dayEnums[index])"
                  :class="[
      form.byDays.includes(dayEnums[index])
        ? 'bg-[#6667D0] text-white'
        : 'bg-gray-100 text-gray-700',
      'w-5 h-5 sm:w-6 sm:h-6 md:w-7 md:h-7 lg:w-8 lg:h-8 rounded-full flex items-center justify-center text-sm font-semibold'
    ]"
                >
                  {{ day }}
                </button>
              </div>
            </div>
            <FieldError :message="errors.byDays" />
            <!-- 시작 날짜 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">시작</label>
              <input
                type="date"
                v-model="form.startDate"
                @focus="showDatePicker = true"
                class="flex-[9_9_0%] text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
              />
            </div>
            <FieldError :message="errors.startDate" />
            <!-- 종료 날짜 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">종료</label>
              <input
                type="date"
                v-model="form.until"
                @focus="showDatePicker = true"
                class="flex-[9_9_0%] text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
              />
            </div>
            <FieldError :message="errors.until" />

            <!-- 시간 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">시간</label>
              <input
                type="time"
                v-model="form.time"
                class="flex-[9_9_0%] text-content-color bg-transparent shadow-sm rounded-md px-3 py-2 transition focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)]"
              />
            </div>
            <FieldError :message="errors.time" />
          </div>
        </div>
      </div>

      <!--타입-->
      <div class="w-full flex justify-end">
        <!-- 오른쪽 영역: 아이콘 + 폼 컨테이너 -->
        <div class="flex w-full max-w-4xl gap-4">
          <!-- 아이콘 -->
          <div class="flex flex-col justify-center h-[41.25px] ">
            <ListTodo class="w-4 h-4 text-content-color" />
          </div>

          <!-- 폼 영역 (세로 배치) -->
          <div class="flex flex-col gap-4 flex-1">
            <!-- 타입 -->
            <div class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">타입</label>
              <div class="flex-[9_9_0%]">
                <SelectBox
                  v-model="form.taskType"
                  :options="typeOptions"
                  :placeholder="typeOptions[0].label"
                  trigger-class="w-full bg-transparent border border-transparent"
                  @update:modelValue="onTaskTypeChange"
                />
              </div>
            </div>

            <!-- 목표값 -->
            <div v-if="form.taskType !== 'CHECK'" class="flex gap-4">
              <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">목표값</label>
              <div class="flex-[9_9_0%]">
              <input
                v-model.number="form.targetValue"
                type="number"
                min="0"
                placeholder="숫자를 입력하세요"
                class="w-full text-base shadow-[0_2px_0_0_rgba(0,0,0,0.05)] px-2 pb-2 border-gray-300 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)] placeholder-gray-400 bg-inherit"
              />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 알림 -->
      <div class="w-full flex justify-end">
        <div class="flex w-full max-w-4xl gap-4">
          <div class="flex flex-col justify-center h-[41.25px] ">
            <Bell class="w-4 h-4 text-content-color" />
          </div>
          <div class="flex gap-4 flex-1">
            <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">알림</label>
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
          <div class="flex flex-col justify-center h-[31.99px] ">
            <StickyNote class="w-4 h-4 text-content-color" />
          </div>
          <div class="flex gap-4 flex-1">
            <label class="flex items-center text-gray-700 font-medium flex-[2.5_2.5_0%]">메모</label>
            <textarea
              v-model="form.memo"
              class="flex-[9_9_0%] text-base shadow-[0_2px_0_0_rgba(0,0,0,0.05)] px-2 pb-2 border-gray-300 focus:outline-none focus:shadow-[0_2px_0_0_theme(colors.main-color/30%)] placeholder-gray-400 resize-none overflow-auto bg-inherit"
              rows="1"
              @input="(e) => emit('autoResize', e)"
            />
          </div>
        </div>
      </div>

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
  </div>
</template>

