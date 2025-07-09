<!-- LoginPage.vue -->
<template>
  <div class="min-h-screen flex justify-center items-start bg-gradient-to-br from-[#f4f5ff] via-[#edf0ff] to-[#e9f3ff] overflow-y-auto">

    <div class="w-[360px] sm:w-[400px] px-8 py-20 bg-white/80 backdrop-blur-lg rounded-3xl shadow-xl">
      <!-- 로고 & 타이틀 -->
      <div class="text-center mb-6">
        <img :src="HeaderIcon" alt="Rouby Logo" class="mx-auto h-10 w-auto" />
        <p class="text-sm text-violet-500 mt-1">나만의 루틴 비서, 루비를 만나보세요!</p>
      </div>

      <!-- 웰컴 문구 -->
      <div class="text-center mb-8">
        <h2 class="text-2xl font-bold text-gray-500">Welcome Back</h2>
        <p class="text-sm text-indigo-500 mt-1 leading-relaxed">
          보석처럼 빛나는 당신의 오늘<br />루비가 언제나 함께합니다.
        </p>
      </div>

      <!-- 이메일 입력 -->
      <label class="block text-sm font-medium text-indigo-600 mb-1">이메일</label>
      <input
        v-model="email"
        type="email"
        placeholder="your@email.com"
        class="w-full mb-4 px-4 py-3 rounded-2xl bg-white/90 border border-violet-100 focus:outline-none focus:ring-2 focus:ring-violet-400"
      />

      <!-- 비밀번호 입력 -->
      <label class="block text-sm font-medium text-indigo-600 mb-1">비밀번호</label>
      <input
        v-model="password"
        type="password"
        placeholder="••••••••"
        class="w-full mb-4 px-4 py-3 rounded-2xl bg-white/90 border border-violet-100 focus:outline-none focus:ring-2 focus:ring-violet-400"
      />

      <!-- 로그인 상태 유지 체크박스 -->
      <label class="inline-flex items-center mb-6 cursor-pointer select-none">
        <input type="checkbox" v-model="staySignedIn" class="accent-violet-600" />
        <span class="ml-2 text-sm text-indigo-600">로그인 상태 유지</span>
      </label>

      <!-- 로그인 버튼 -->
      <button
        @click="onLogin"
        class="w-full py-3 rounded-2xl text-white font-semibold bg-gradient-to-r from-[#6D6AFF] to-[#B18CFF] hover:brightness-105 transition"
      >
        로그인
      </button>

      <!-- 구분선 -->
      <div class="relative my-8 text-center">
        <span class="px-2 bg-white/80"></span>
        <div class="absolute inset-0 flex items-center" aria-hidden="true">
          <div class="w-full border-t border-gray-200"></div>
        </div>
      </div>

      <!-- 소셜 로그인 -->
      <div class="flex justify-between gap-4 mb-6">
        <button class="w-full py-3 rounded-2xl border border-[#D5C8FF] hover:bg-violet-50 transition">
          <!-- 소셜 아이콘 -->
          <span class="text-xl">😊</span>
        </button>
        <button class="w-full py-3 rounded-2xl border border-[#D5C8FF] hover:bg-violet-50 transition">
          <span class="text-xl">🌐</span>
        </button>
        <button class="w-full py-3 rounded-2xl border border-[#D5C8FF] hover:bg-violet-50 transition">
          <span class="text-xl">🍎</span>
        </button>
      </div>

      <!-- 하단 링크 -->
      <div class="text-center text-xs text-[#B6A6FF] space-x-2">
        <a href="#" class="hover:underline">비밀번호 찾기</a>
        <span>|</span>
        <a href="#" class="hover:underline">아이디 찾기</a>
        <span>|</span>
        <a href="#" class="hover:underline">회원가입</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import HeaderIcon from "@/assets/header_logo.svg";

const email = ref('');
const password = ref('');
const staySignedIn = ref(false);

function onLogin() {
  alert(`로그인 시도\n이메일: ${email.value}\n비밀번호: ${password.value}\n상태 유지: ${staySignedIn.value}`);
}
</script>
