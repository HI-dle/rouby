<script setup>
import { nextTick, onMounted, ref } from 'vue'
import { CalendarClock, Calendar, Bell, RefreshCw } from 'lucide-vue-next'
import SelectBox from '@/components/common/SelectBox.vue'
import ToggleSwitch from '@/components/common/ToggleSwitch.vue'
import { alarmOptions, repeatOptions } from '../constants'
import { toDate } from '@/shared/utils/formatDate'
import {} from 'vue'

const props = defineProps({
  form: Object,
  errors: Object,
})
const emit = defineEmits(['submit', 'input-datetime'])

const autoResize = (e) => {
  const el = e.target
  if (!el) return
  el.style.height = 'auto'
  el.style.height = `${el.scrollHeight}px`
}
</script>

<template>
  <form @submit.prevent="emit('submit')" class="w-full">
    <div class="min-h-[100vh] max-w-md mx-auto p-4 space-y-6 bg-white main-container rounded-t-2xl">
      <!-- 제목 -->
      <div>
        <input
          v-model="form.title"
          type="text"
          placeholder="제목"
          class="w-full text-base font-semibold border-b px-2 pb-2 mt-4 border-gray-300 focus:outline-none focus:border-black placeholder-gray-400 bg-inherit"
        />
        <p v-if="errors.title" class="text-sm text-error-color mt-1 mx-2">{{ errors.title }}</p>
      </div>

      <!-- 메모 -->
      <textarea
        v-model="form.memo"
        placeholder="메모"
        class="w-full text-base border-b px-2 pb-2 border-gray-300 focus:outline-none focus:border-black resize-none placeholder-gray-400 overflow-hidden bg-inherit"
        rows="1"
        @input="autoResize"
      />

      <div class="flex flex-row">
        <div class="mx-2 h-9 flex items-center justify-center">
          <CalendarClock class="w-4 h-4 text-content-color" />
        </div>
        <div class="space-y-2 w-full">
          <!-- 하루종일 토글 -->
          <div class="flex items-center justify-between mx-2 h-9">
            <span class="text-base font-semibold text-content-color">하루종일</span>
            <ToggleSwitch v-model="form.allDay" />
          </div>

          <!-- 시작/종료 -->
          <div class="flex justify-between items-center mx-2">
            <label class="text-base font-semibold text-content-color">시작</label>
            <input
              :type="form.allDay ? 'date' : 'datetime-local'"
              :value="form.allDay ? toDate(form.start) : form.start"
              @input="(e) => emit('input-datetime', e, 'start')"
              class="w-60 text-base text-content-color border rounded-md px-3 py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
            />
          </div>
          <div class="flex justify-between items-center mx-2">
            <label class="text-base font-semibold text-content-color">종료</label>
            <input
              :type="form.allDay ? 'date' : 'datetime-local'"
              :value="form.allDay ? toDate(form.end) : form.end"
              @input="(e) => emit('input-datetime', e, 'end')"
              class="w-60 text-base text-content-color border rounded-md px-3 py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
            />
          </div>
          <p v-if="errors.period" class="text-sm text-error-color mt-1 mx-2">{{ errors.period }}</p>
        </div>
      </div>

      <hr />

      <!-- 알림 -->
      <div class="flex justify-between items-center mr-2">
        <div class="flex justify-normal items-center">
          <div class="mx-2 h-9 flex items-center justify-center">
            <Bell class="w-4 h-4 text-content-color" />
          </div>
          <label class="ml-2 text-base font-semibold text-content-color">알림</label>
        </div>
        <SelectBox
          v-model="form.alarmOffsetMinutes"
          :options="alarmOptions"
          :placeholder="alarmOptions[0].label"
        />
      </div>

      <!-- 반복 -->
      <div class="flex justify-between items-center mr-2">
        <div class="flex justify-normal items-center">
          <div class="mx-2 h-9 flex items-center justify-center">
            <RefreshCw class="w-4 h-4 text-content-color" />
          </div>
          <label class="ml-2 text-base font-semibold text-content-color">반복</label>
        </div>
        <SelectBox
          v-model="form.repeat"
          :options="repeatOptions"
          :placeholder="repeatOptions[0].label"
        />
      </div>

      <!-- 루틴 시작 -->
      <div class="flex justify-between items-center mr-2">
        <div class="flex justify-normal items-center">
          <div class="mx-2 h-9 flex items-center justify-center">
            <Calendar class="w-4 h-4 text-content-color" />
          </div>
          <label class="ml-2 text-base font-semibold text-content-color">루틴 시작</label>
        </div>
        <input
          type="date"
          v-model="form.routineStart"
          class="text-base text-content-color border rounded-md px-3 py-2 w-40 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
        />
        <p v-if="errors.routineStart" class="text-sm text-error-color mt-1 mx-2">
          {{ errors.routineStart }}
        </p>
      </div>

      <!-- 버튼 -->
      <div class="flex justify-evenly pt-10">
        <button class="w-44 *:px-4 py-2 rounded-lg bg-gray-100 text-gray-700">취소</button>
        <button @click="onSubmit" class="w-44 px-4 py-2 rounded-lg bg-main-color text-white">
          저장
        </button>
      </div>
    </div>
  </form>
</template>
