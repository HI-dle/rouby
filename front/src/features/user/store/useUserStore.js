import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const id = ref(null)
  const email = ref('')
  const nickname = ref('')
  const healthStatusKeywords = ref([])
  const profileKeywords = ref([])
  const communicationTone = ref([])

  function setUserInfo(userInfo) {
    id.value = userInfo.id
    email.value = userInfo.email
    nickname.value = userInfo.nickname
    healthStatusKeywords.value = userInfo.healthStatusKeywords || []
    profileKeywords.value = userInfo.profileKeywords || []
    communicationTone.value = userInfo.communicationTone || []
  }

  return {
    id,
    email,
    nickname,
    healthStatusKeywords,
    profileKeywords,
    communicationTone,
    setUserInfo,
  }
})
