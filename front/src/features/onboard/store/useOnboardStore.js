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
  const scheduleAlarm = ref(false)
  const routineAlarm = ref(true)
  const briefingAlarm = ref(true)

  return {
    userName,
    selectedHealth,
    personalKeyword,
    startOfDayTime,
    endOfDayTime,
    speechType,
    scheduleAlarm,
    routineAlarm,
    briefingAlarm,
  }
},{
  persist: true,
})
