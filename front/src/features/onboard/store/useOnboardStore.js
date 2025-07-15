// stores/useOnboardStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOnboardStore = defineStore('onboard', () => {
  const userName = ref('')
  const selectedHealth = ref('')
  const personalKeywords = ref('')

  return {
    userName,
    selectedHealth,
    personalKeywords,
  }
},{
  persist: true,
})
