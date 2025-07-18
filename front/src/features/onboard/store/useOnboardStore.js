// stores/useOnboardStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOnboardStore = defineStore('onboard', () => {
  const userName = ref('')
  const selectedHealth = ref('')
  const personalKeyword = ref('')
  const startOfDayTime = ref('')
  const endOfDayTime = ref('')
  const speechType = ref('')

  return {
    userName,
    selectedHealth,
    personalKeyword,
    startOfDayTime,
    endOfDayTime,
    speechType,
  }
},{
  persist: true,
})
