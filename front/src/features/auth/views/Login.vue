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
        class="w-full mb-1 px-4 py-3 rounded-2xl bg-white/90 border border-violet-100 focus:outline-none focus:ring-2 focus:ring-violet-400"
        @keyup.enter="onLogin"
      />
      <p v-if="emailError" class="text-xs text-red-500 mb-3">{{ emailError }}</p>

      <!-- 비밀번호 입력 -->
      <label class="block text-sm font-medium text-indigo-600 mb-1">비밀번호</label>
      <input
        v-model="password"
        type="password"
        placeholder="••••••••"
        class="w-full mb-1 px-4 py-3 rounded-2xl bg-white/90 border border-violet-100 focus:outline-none focus:ring-2 focus:ring-violet-400"
        @keyup.enter="onLogin"
      />
      <p v-if="passwordError" class="text-xs text-red-500 mb-3">{{ passwordError }}</p>

      <!-- 로그인 상태 유지 체크박스 -->
      <label class="inline-flex items-center mb-6 cursor-pointer select-none">
        <input type="checkbox" v-model="staySignedIn" class="accent-violet-600" />
        <span class="ml-2 text-sm text-indigo-600">로그인 상태 유지</span>
      </label>

      <!-- 로그인 실패 메시지 -->
      <p v-if="loginError" class="text-sm text-red-500 text-center mb-4">
        {{ loginError }}
      </p>

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
        <!-- 카카오 로그인 -->
        <button class="w-full py-3 rounded-2xl border border-[#D5C8FF] hover:bg-violet-50 transition flex justify-center items-center">
          <img src="@/features/auth/icon/kakao.svg" alt="Kakao Login" class="h-6" />
        </button>

        <!-- 구글 로그인 -->
        <button class="w-full py-3 rounded-2xl border border-[#D5C8FF] hover:bg-violet-50 transition flex justify-center items-center">
          <img src="@/features/auth/icon/google.svg" alt="Google Login" class="h-6" />
        </button>

        <!-- 애플 로그인 -->
        <button class="w-full py-3 rounded-2xl border border-[#D5C8FF] hover:bg-violet-50 transition flex justify-center items-center">
          <img src="@/features/auth/icon/apple.svg" alt="Apple Login" class="h-6" />
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
import { useRouter } from 'vue-router';
import HeaderIcon from "@/assets/header_logo.svg";
import { storeToken } from '@/features/auth/storeToken';
import { login } from '@/features/auth/loginApi';


// 입력값 및 상태
const email = ref('');
const password = ref('');
const staySignedIn = ref(false);

// 에러 메시지
const emailError = ref('');
const passwordError = ref('');
const loginError = ref('');

const router = useRouter();

// 이메일 형식 검사 함수
function validateEmailFormat(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// 로그인 함수
async function onLogin() {
  // 에러 초기화
  emailError.value = '';
  passwordError.value = '';
  loginError.value = '';

  // 이메일 검사
  if (!validateEmailFormat(email.value)) {
    emailError.value = '잘못된 이메일 형식입니다.';
    return;
  }

  // 비밀번호 검사
  if (!password.value) {
    passwordError.value = '비밀번호를 입력해주세요.';
    return;
  }

  try {
    const token = await login({ email: email.value.trim(), password: password.value });

    storeToken(token, staySignedIn.value);

    await router.push('/');
  } catch (error) {
    loginError.value = '아이디 혹은 비밀번호가 일치하지 않습니다. 다시 확인해주세요.';
    console.error('로그인 실패:', error);
  }
}
</script>
