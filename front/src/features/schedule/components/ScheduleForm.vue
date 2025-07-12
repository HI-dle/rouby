<script setup>
import { CalendarClock, Calendar, Bell, RefreshCw } from 'lucide-vue-next'
import SelectBox from '@/components/common/SelectBox.vue'
import ToggleSwitch from '@/components/common/ToggleSwitch.vue'
import { alarmOptions, repeatOptions } from '../constants'
import { toDate } from '@/shared/utils/formatDate'
import FieldError from '@/components/common/FieldError.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseModal from '@/components/common/BaseModal.vue'

const props = defineProps({
  form: Object,
  errors: Object,
  errorModal: Object,
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
          class="w-full text-base font-semibold border-b px-2 pb-2 mt-4 border-border-color focus:outline-none focus:border-black placeholder-gray-400 bg-inherit"
        />
        <FieldError :message="errors.title" />
      </div>

      <!-- 메모 -->
      <textarea
        v-model="form.memo"
        placeholder="메모"
        class="w-full text-base border-b px-2 pb-2 border-border-color focus:outline-none focus:border-black placeholder-gray-400 resize-none overflow-auto max-h-[30vh] bg-inherit"
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
            class="w-full xxs:w-40 xxxs:w-[123px] text-base xs:text-sm xxs:text-xs text-content-color border border-transparent rounded-md px-3 xxs:px-[5px] py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
          />

          <!-- 종료 -->
          <div class="flex items-center mx-2">
            <label class="ml-8 text-base font-semibold text-content-color">종료</label>
          </div>
          <input
            :type="form.allDay ? 'date' : 'datetime-local'"
            :value="form.allDay ? toDate(form.end) : form.end"
            @input="(e) => emit('inputDatetime', e, 'end')"
            class="w-full xxs:w-40 xxxs:w-[123px] text-base xs:text-sm xxs:text-xs text-content-color border border-transparent rounded-md px-3 xxs:px-[5px] py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
          />
        </div>
        <!-- 에러 메시지 -->
        <FieldError :message="errors.period" />
      </div>
      <hr class="bg-border-color" />

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
            class="w-full xxxs:w-[120px] text-base xxs:text-xs text-content-color border border-transparent rounded-md px-3 py-2 shadow-sm focus:outline-none focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition"
          />
        </div>
        <FieldError :message="errors.routineStart" />
      </div>
      <!-- 버튼 -->
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
  <BaseModal v-model="errorModal.show" :message="errorModal.msg" />
</template>
