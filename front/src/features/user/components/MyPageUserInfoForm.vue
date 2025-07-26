<script setup>
import {onMounted} from 'vue'
import UserSettingInput from '@/components/common/UserSettingInput.vue'
import {useUserInfoForm} from '@/features/user/useUserInfoForm.js'
import KeywordTag from '@/components/common/KeywordTag.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import FieldError from '@/components/common/FieldError.vue'
import BaseInput from "@/components/common/BaseInput.vue";

const {
  healthStatusKeywords,
  profileKeywords,
  keywordError,
  healthStatusKeyword,
  profileKeyword,
  nickname,
  dailyStartTime,
  dailyEndTime,
  loadInitialSettings,
  saveSettings,
  addHealthStatusKeywordsTag,
  removeHealthStatusKeywordsTag,
  addProfileKeywordsTag,
  removeProfileKeywordsTag
} = useUserInfoForm()

onMounted(() => {
  loadInitialSettings()
})

</script>

<template>
  <div class="w-full max-w-xl mx-auto space-y-8">
    <div class="flex flex-wrap gap-2 mt-4">
      <h2 class="text-lg font-semibold text-content-color">이름 설정</h2>
      <BaseInput
        v-model="nickname"
        type="text"
        placeholder="이름"
        class="mt-6"
      />
    </div>
    <!-- 구분선 & 간격 -->
    <hr class="my-6 border-t border-gray-300" />
    <!-- 건강 상태 메모 -->
    <div class="flex flex-wrap gap-2 mt-4">
      <div class="w-[100%]"><h2 class="text-lg font-semibold text-content-color">건강 상태 메모</h2></div>
      <KeywordTag
        v-for="tag in healthStatusKeywords"
        :key="tag"
        :label="tag"
        @remove="removeHealthStatusKeywordsTag(tag)"
      />
    </div>

    <!-- 입력창 -->
    <div class="mt-6">
      <UserSettingInput
        v-model="healthStatusKeyword"
        placeholder="ex. 아토피, ADHD, 우울증, 유당불내증"
        @submit="addHealthStatusKeywordsTag"
        :error="keywordError"
      />
    </div>
    <FieldError :message="keywordError" />

    <!-- 구분선 & 간격 -->
    <hr class="my-6 border-t border-gray-300" />

    <div class="flex flex-wrap gap-2 mt-4">
      <div class="w-[100%]"><h2 class="text-lg font-semibold text-content-color">관심 태그</h2></div>
      <KeywordTag
        v-for="tag in profileKeywords"
        :key="tag"
        :label="tag"
        @remove="removeProfileKeywordsTag(tag)"
      />
    </div>

    <!-- 입력창 -->
    <div class="mt-6">
      <UserSettingInput
        v-model="profileKeyword"
        placeholder="ex. 다이어트, 개발, 헬스, 요리"
        @submit="addProfileKeywordsTag"
        :error="keywordError"
      />
    </div>
    <FieldError :message="keywordError" />


    <!-- 구분선 & 간격 -->
    <hr class="my-6 border-t border-gray-300" />

    <!-- 알림 설정 -->
    <div class="space-y-4">
      <h2 class="text-lg font-semibold text-content-color">내 하루 설정</h2>

      <div class="flex items-center justify-between">
        <span class="text-sm text-content-color">하루 시작 시간</span>
        <input
          type="time"
          v-model="dailyStartTime"
          class="input bg-white w-[25%] min-w-[140px] p-3 border-border-color border rounded-2xl text-main-color h-12 placeholder-placeholder-pink-color"
        />
      </div>

      <div class="flex items-center justify-between">
        <span class="text-sm text-content-color">하루 마감 시간</span>
        <input
          type="time"
          v-model="dailyEndTime"
          class="input bg-white w-[25%] min-w-[140px] p-3 border-border-color border rounded-2xl text-main-color h-12 placeholder-placeholder-pink-color"
        />
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

