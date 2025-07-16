// stores/useOnboardStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOnboardStore = defineStore('onboard', () => {
  const userName = ref('')
  const selectedHealth = ref('')
  const personalKeyword = ref('')
  const startOfDayTime = ref('')

  return {
    userName,
    selectedHealth,
    personalKeyword,
    startOfDayTime,
  }
},{
  persist: true,
})
