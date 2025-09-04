import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { withdrawalOfUser } from '@/features/user/userService.js'
import { useUserInfoStore } from '@/stores/useUserInfoStore'
import { resetAllStores } from '@/shared/utils/piniaUtils'

export function useMyPageForm() {
  const router = useRouter()
  const store = useUserInfoStore()
  const nickname = computed(() => store.nickname)
  const showConfirmModal = ref(false)
  const showErrorModal = ref(false)
  const isWithdrawing = ref(false)
  const errors = reactive({
    apiResult: '',
  })

  const menuItems = [
    { label: '내 정보 설정', route: { name: 'mypage' } },
    { label: '비밀번호 변경', route: { name: 'password-reset' } },
    { label: '루비 설정', route: { name: 'rouby-setting' } },
    { label: '회원탈퇴', action: 'withdraw' },
  ]

  const goTo = (route) => {
    router.push(route)
  }

  const handleMenuClick = (item) => {
    if (item.action === 'withdraw') {
      showConfirmModal.value = true
    } else if (item.route) {
      goTo(item.route)
    }
  }

  const onConfirmWithdraw = async () => {
    isWithdrawing.value = true
    try {
      resetAllStores()
      removeStorageFlagForPinia()

      const success = await withdrawalOfUser()
      if (success) {
        goTo({ name: 'login' })
      }
    } catch (err) {
      if (err.fieldErrors) {
        Object.assign(errors, err.fieldErrors)
      } else {
        errors.apiResult = '회원 탈퇴에 실패 하였습니다.'
      }
      showErrorModal.value = true
    } finally {
      isWithdrawing.value = false
    }
  }

  return {
    nickname,
    errors,
    menuItems,
    showConfirmModal,
    showErrorModal,
    handleMenuClick,
    onConfirmWithdraw,
  }
}
