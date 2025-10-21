<script setup>
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import { MOODS } from '../constants'

const { messages } = defineProps({
  messages: { type: Array, default: [], required: true },
})
</script>

<template>
  <div class="flex items-center justify-around">
    <div
      class="flex flex-col space-y-4 w-full max-w-xl mx-auto mb-8 overflow-y-auto"
    >
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="[
          'flex',
          {
            'justify-end': msg.from == 'user',
            'justify-start': msg.from == 'rouby',
          },
        ]"
      >
        <div
          class="flex flex-col max-w-[80%]"
          :class="[
            {
              'from-user': msg.from == 'user',
            },
          ]"
        >
          <p
            v-if="msg.time"
            class="flex text-xs text-gray-500 mt-[-0.5rem](-8px) mb-1"
            :class="[
              {
                'justify-end mr-1': msg.from == 'user',
                'justify-start ml-1': msg.from == 'rouby',
              },
            ]"
          >
            {{ msg.time }}
          </p>
          <div
            class="px-4 py-2 bg-white border border-placeholder-color/20 rounded-2xl shadow-sm min-w-20 text-content-color text-left break-words"
            :class="[
              {
                'bg-gradient-to-r from-button-from to-button-to text-white !w-auto':
                  msg.from == 'rouby',
              },
            ]"
          >
            <div class="flex justify-end">
              <div
                v-if="msg.mood"
                class="mb-1 py-1 px-2 shadow-sm shadow-placeholder-color/30 rounded-xl text-center text-main-color font-medium"
              >
                {{ MOODS[msg.mood.toLowerCase()]?.day || msg.mood }}
              </div>
            </div>
            <div class="flex items-center">
              <div class="pr-2" v-html="msg.txt"></div>
              <LoadingSpinner v-if="msg.ready" :size="15" stroke-width="2.5" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>
