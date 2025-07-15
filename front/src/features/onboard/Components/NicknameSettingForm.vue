
<template>
  <div class="text-center mt-12 space-y-6 w-full text-main-color">
    <div>
      <p class="text-base font-semibold">만나서 반가워요!</p>
      <p class="text-base font-semibold">제가 뭐라고 부르면 될까요?</p>
      <p class="text-sm text-#6667D07A mt-2">루비와 함께할 분의 이름을 알고싶어요!</p>
    </div>

    <!-- 문장과 inline 인풋 -->
    <div class="text-base mt-6 text-main-color">
      나를
      <input
        v-model="nickname"
        type="text"
        placeholder="루비"
        :style="{
          width: nickname.length <= 4 ? '96px' : `${nickname.length + 1}ch`,
          minWidth: '96px',
          maxWidth: '200px',
          boxShadow: nicknameError && isFocused ? '0 0 0 3px rgba(255, 72, 66, 0.3)' : '',
          transition: 'width 0.2s ease',
        }"
        class="inline-block text-center mx-2 px-3 py-1 rounded-full bg-white border text-main-color placeholder:text-placeholder-color text-base outline-none focus:ring-2 transition shadow-sm"
        :class="nicknameError ? 'border-error-color focus:ring-error-color' : 'border-border-color focus:ring-[#B6A6FF]'"
        @input="validateNickname"
        @focus="isFocused.value = true"
        @blur="isFocused.value = false"
      />
      라고 불러줘
    </div>

    <FieldError v-if="nicknameError" :message="nicknameError" class="mt-2" />
  </div>
</template>

<!-- components/onboarding/HealthKeywordForm.vue -->
<template>
  <div class="text-center mt-12 space-y-6 w-full text-indigo-600">
    <div>
      <p class="text-base">루비를 시작하기 전,</p>
      <p class="text-base">{{ userName }}님의 건강 상태에 대해 알고싶어요!</p>
      <p class="text-sm text-indigo-300 mt-2">여러 키워드를 추가할 수 있어요!</p>
    </div>

    <!-- 키워드 태그 목록 -->
    <div class="flex flex-wrap justify-center gap-2 mt-4">
      <KeywordTag
        v-for="k in keywords"
        :key="k"
        :label="k"
        @remove="removeKeyword(k)"
      />
    </div>

    <!-- 입력창 -->
    <div class="mt-10">
      <UserSettingInput
        v-model="keyword"
        placeholder="키워드를 추가해주세요."
        @submit="handleSubmit"
        :error="keywordError"
      />
      <FieldError :message="keywordError" />
      <p class="float-left text-s text-indigo-300 mt-2 ml-2.5">
        ex) {{ exampleKeywords.join(', ') }}
      </p>
    </div>
  </div>
</template>

<script setup>
import FieldError from '@/components/common/FieldError.vue'
import { useNicknameForm } from '@/features/onboard/useNicknameForm.js'

const {
  nickname,
  nicknameError,
  isFocused,
  validateNickname,
} = useNicknameForm()
</script>

