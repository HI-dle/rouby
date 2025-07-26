<template>
  <div
    class="w-full min-h-screen flex flex-col justify-between bg-gradient-to-b from-white to-violet-50"
  >
    <!-- 상단 콘텐츠 -->
    <div class="px-6 pt-14">
      <div class="text-center space-y-2">
        <figure>
          <img
            :src="HeaderIcon"
            alt="헤더 아이콘"
            class="mx-auto h-10 w-auto"
          />
        </figure>
        <p class="text-lg font-bold text-main-color">
          {{ nickname }}님, 안녕하세요
        </p>
      </div>
      <div class="mt-10 divide-y divide-gray-200">
        <div
          v-for="(item, index) in menuItems"
          :key="index"
          class="flex justify-between items-center py-4 cursor-pointer hover:bg-gray-100 transition"
          @click="handleMenuClick(item)"
        >
          <span class="text-base text-content-color">{{ item.label }}</span>
          <span class="text-gray-400">➔</span>
        </div>
      </div>
    </div>

    <BaseModal
      v-model="showConfirmModal"
      message="정말 탈퇴 하시겠습니까?"
      buttonText="탈퇴하기"
      @withdrawal="onConfirmWithdraw"
    />

    <BaseModal
      v-model="showErrorModal"
      :message="errors.apiResult"
      buttonText="확인"
    />
  </div>
</template>

<script setup>
import HeaderIcon from '@/assets/header_logo.svg'
import BaseModal from '@/components/common/BaseModal.vue'
import { useMyPageForm } from '@/features/user/useMyPageForm.js'

const {
  nickname,
  errors,
  menuItems,
  showConfirmModal,
  showErrorModal,
  handleMenuClick,
  onConfirmWithdraw,
} = useMyPageForm()
</script>
