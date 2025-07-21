<script setup>
import { onMounted } from 'vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import ToggleSwitch from '@/components/common/ToggleSwitch.vue'
import { useRoubySettingForm } from '@/features/user/useRoubySettingForm.js'
import KeywordTag from '@/components/common/KeywordTag.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import FieldError from '@/components/common/FieldError.vue'

const {
  communicationTone,
  keywordError,
  keyword,
  notifyBeforeRoutine,
  notifyMorningBriefing,
  notifyBeforeSchedule,
  loadInitialSettings,
  saveSettings,
  addToneTag,
  removeToneTag
} = useRoubySettingForm()

onMounted(() => {
  loadInitialSettings()
})

</script>

<template>
  <div class="w-full max-w-xl mx-auto space-y-8">
    <h2 class="text-lg font-semibold text-content-color">말투 설정</h2>
    <!-- 커뮤니케이션 톤 태그 리스트 -->
    <div class="flex flex-wrap gap-2 mt-4">
      <KeywordTag
        v-for="tag in communicationTone"
        :key="tag"
        :label="tag"
        @remove="removeToneTag(tag)"
      />
    </div>

    <!-- 입력창 -->
    <div class="mt-6">
      <UserSettingInput
        v-model="keyword"
        placeholder="ex. 귀여운, 공손한"
        @submit="addToneTag"
        :error="keywordError"
      />
    </div>
    <FieldError :message="keywordError" />
    <!-- 구분선 & 간격 -->
    <hr class="my-6 border-t border-gray-300" />

    <!-- 알림 설정 -->
    <div class="space-y-4">
      <h2 class="text-lg font-semibold text-content-color">알림 설정</h2>

      <div class="flex items-center justify-between">
        <span class="text-sm text-content-color">하루 시작 브리핑알림</span>
        <ToggleSwitch v-model="notifyMorningBriefing" />
      </div>

      <div class="flex items-center justify-between">
        <span class="text-sm text-content-color">일정 시작 전 알림</span>
        <ToggleSwitch v-model="notifyBeforeSchedule" />
      </div>

      <div class="flex items-center justify-between">
        <span class="text-sm text-content-color">루틴 시작 전 알림</span>
        <ToggleSwitch v-model="notifyBeforeRoutine" />
      </div>
    </div>

    <!-- 버튼 -->
    <div class="flex justify-between pt-10 gap-2">
      <BaseButton
        @click="emit('cancel')"
        class="bg-none bg-gray-200 !text-content-color hover:bg-gray-300"
      >취소
      </BaseButton>
      <BaseButton type="button" @click="saveSettings">저장</BaseButton>
    </div>
  </div>
</template>

