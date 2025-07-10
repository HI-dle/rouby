<script setup>
import { CalendarClock, Calendar, Bell, RefreshCw } from 'lucide-vue-next'
import SelectBox from '@/components/common/SelectBox.vue'
import ToggleSwitch from '@/components/common/ToggleSwitch.vue'
import { alarmOptions, repeatOptions } from '../constants'
import { toDate } from '@/shared/utils/formatDate'

const props = defineProps({
  form: Object,
  errors: Object,
})
const emit = defineEmits(['submit', 'cancel', 'inputDatetime', 'autoResize'])
</script>

<template>
  <div class="main-container">
    <form
      @submit.prevent="emit('submit')"
      class="sub-main-container space-y-6 rounded-2xl bg-white"
    >
      <!-- 제목 -->
      <div>
        <input
          v-model="form.title"
          type="text"
          placeholder="제목"
          class="w-full text-base font-semibold border-b px-2 pb-2 mt-4 border-gray-300 focus:outline-none focus:border-black placeholder-gray-400 bg-inherit"
        />
        <p v-if="errors.title" class="text-sm text-error-color mt-2 mx-2">{{ errors.title }}</p>
      </div>

      <!-- 메모 -->
      <textarea
        v-model="form.memo"
        placeholder="메모"
        class="w-full text-base border-b px-2 pb-2 border-gray-300 focus:outline-none focus:border-black placeholder-gray-400 resize-none overflow-auto max-h-[30vh] bg-inherit"
        rows="1"
        @input="(e) => emit('autoResize', e)"
      />

      <div>
        <div class="grid grid-cols-[2fr_4fr] gap-4 xxs:grid-cols-[4fr_4fr]">
          <!-- 하루종일 -->
          <div class="flex justify-normal items-center">
            <div class="flex items-center gap-2 mx-2 h-9">
              <CalendarClock class="w-4 h-4 text-content-color" />
            </div>
            <label class="ml-2 text-base font-semibold text-content-color">하루종일</label>
          </div>
          <div class="flex items-center justify-end mx-2">
            <ToggleSwitch v-model="form.allDay" />
          </div>

          <!-- 시작 -->
          <div class="flex items-center mx-2">
            <label class="ml-8 text-base font-semibold text-content-color">시작</label>
          </div>
          <input
            :type="form.allDay ? 'date' : 'datetime-local'"
            :value="form.allDay ? toDate(form.start) : form.start"
            @input="(e) => emit('input-datetime', e, 'start')"
            class="w-full xxs:w-40 xxxs:w-[123px] text-base xs:text-sm xxs:text-xs text-content-color border rounded-md px-3 xxs:px-[5px] py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
          />

          <!-- 종료 -->
          <div class="flex items-center mx-2">
            <label class="ml-8 text-base font-semibold text-content-color">종료</label>
          </div>
          <input
            :type="form.allDay ? 'date' : 'datetime-local'"
            :value="form.allDay ? toDate(form.end) : form.end"
            @input="(e) => emit('inputDatetime', e, 'end')"
            class="w-full xxs:w-40 xxxs:w-[123px] text-base xs:text-sm xxs:text-xs text-content-color border rounded-md px-3 xxs:px-[5px] py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
          />
        </div>
        <!-- 에러 메시지 -->
        <p v-if="errors.period" class="text-sm text-error-color mt-2 mx-2">{{ errors.period }}</p>
      </div>
      <hr />

      <!-- 알림 -->
      <div class="grid grid-cols-[2fr_4fr] gap-4 xxs:grid-cols-[4fr_4fr]">
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
          trigger-class="w-full"
        />
      </div>

      <!-- 반복 -->
      <div class="grid grid-cols-[2fr_4fr] gap-4 xxs:grid-cols-[4fr_4fr]">
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
          trigger-class="w-full"
        />
      </div>

      <!-- 루틴 시작 -->
      <div>
        <div class="grid grid-cols-[2fr_4fr] gap-4 xxs:grid-cols-[4fr_4fr]">
          <div class="flex justify-normal items-center">
            <div class="mx-2 h-9 flex items-center justify-center">
              <Calendar class="w-4 h-4 text-content-color" />
            </div>
            <label class="ml-2 text-base font-semibold text-content-color">루틴 시작</label>
          </div>
          <input
            type="date"
            v-model="form.routineStart"
            class="w-full xxxs:w-[120px] text-base xxs:text-xs text-content-color border rounded-md px-3 py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
          />
        </div>
        <p v-if="errors.routineStart" class="text-sm text-error-color mt-2 mx-2">
          {{ errors.routineStart }}
        </p>
      </div>

      <!-- 버튼 -->
      <div class="flex justify-between pt-10 gap-2">
        <button
          type="button"
          @click="() => emit('cancel')"
          class="w-full px-4 py-2 rounded-lg bg-gray-100 text-gray-700"
        >
          취소
        </button>
        <button type="submit" class="w-full px-4 py-2 rounded-lg bg-main-color text-white">
          저장
        </button>
      </div>
    </form>
  </div>
</template>
