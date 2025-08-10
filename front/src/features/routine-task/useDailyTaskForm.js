import { reactive, onMounted, onBeforeUnmount } from 'vue'
import { debounce } from 'lodash-es'
import { wrapApi } from '@/shared/utils/errorUtils.js'
import { typeOptions, alarmOptions } from './constants'
import { progressDailyTask } from './RoutineTaskService.js'

export function useDailyTaskForm(task) {
  const uiState = reactive({
    displayValue: task.currentValue,
    errorMessage: '',
    fieldErrors: {},
    lastSavedValue: task.currentValue,
    syncStatus: 'synced',
    inFlight: false,
    queuedValue: null,
  })

  const updateTaskProgressApi = wrapApi(progressDailyTask, {
    fallbackMessage: '저장에 실패했습니다. 다시 시도해주세요.',
  })

  let lastSentAt = 0
  const MIN_GAP = 800 // ms: 실제 전송 간 최소 간격

  const actuallySave = async (value) => {
    const now = Date.now()
    const gap = now - lastSentAt
    if (gap < MIN_GAP) {
      // 최소간격 미달이면 남은 시간 뒤에 실행
      setTimeout(() => actuallySave(value), MIN_GAP - gap)
      return
    }

    uiState.inFlight = true
    lastSentAt = Date.now()
    try {
      const req = {
        id: task.id,
        routineTaskId: task.routineTaskId,
        currentValue: value,
        taskDate: task.date,
      }
      const res = await updateTaskProgressApi(req)
      task.id = res.id
      task.currentValue = value
      uiState.lastSavedValue = value
      uiState.syncStatus = 'synced'
    } catch (err) {
      uiState.syncStatus = 'failed'
      uiState.fieldErrors = err.fieldErrors ?? {}
      uiState.errorMessage = err.message ?? ''
    } finally {
      uiState.inFlight = false
      if (uiState.queuedValue !== null && uiState.queuedValue !== uiState.lastSavedValue) {
        const next = uiState.queuedValue
        uiState.queuedValue = null
        uiState.syncStatus = 'pending'
        actuallySave(next) // 직렬 재전송
      }
    }
  }

  // COUNT/MINUTES용: 길게 연타해도 2초마다 최소 1회만 전송
  const debouncedSaveNumeric = debounce((value) => {
    if (uiState.inFlight) uiState.queuedValue = value
    else actuallySave(value)
  }, 600, { leading: false, trailing: true, maxWait: 2000 })

  // CHECK용: 짧게 튕겨주는 디바운스
  const debouncedSaveCheck = debounce((value) => {
    if (uiState.inFlight) uiState.queuedValue = value
    else actuallySave(value)
  }, 150, { leading: false, trailing: true, maxWait: 1000 })

  const scheduleSaveNumeric = (value) => {
    uiState.syncStatus = 'pending'
    if (uiState.inFlight) uiState.queuedValue = value
    else debouncedSaveNumeric(value)
  }

  const scheduleSaveCheck = (value) => {
    uiState.syncStatus = 'pending'
    if (uiState.inFlight) uiState.queuedValue = value
    else debouncedSaveCheck(value)
  }

  // CHECK
  const handleCheckboxChange = (event) => {
    const newValue = event.target.checked ? 1 : 0
    uiState.displayValue = newValue
    if (newValue !== uiState.lastSavedValue) scheduleSaveCheck(newValue)
    else uiState.syncStatus = 'synced'
  }

  // COUNT/MINUTES
  const MIN_VALUE = 0
  const MAX_VALUE = 999
  const handleValueChange = (newValue) => {
    if (newValue < MIN_VALUE || newValue > MAX_VALUE) return
    uiState.displayValue = newValue
    if (newValue !== uiState.lastSavedValue) scheduleSaveNumeric(newValue)
    else uiState.syncStatus = 'synced'
  }

  const clearError = () => {
    uiState.errorMessage = ''
    uiState.fieldErrors = {}
  }

  const getUnitLabel = () => {
    switch (task.taskType) {
      case 'COUNT': return '회'
      case 'MINUTES': return '분'
      default: return ''
    }
  }
  const getTaskTypeLabel = () => {
    const opt = typeOptions.find(o => o.value === task.taskType)
    return opt?.label || '알 수 없음'
  }
  const getAlarmLabel = () => {
    const opt = alarmOptions.find(o => o.value === task.alarmOffsetMinutes)
    return opt?.label || '사용자 설정'
  }

  // 페이지 이탈 전 마지막 저장
  const handleBeforeUnload = (e) => {
    debouncedSaveNumeric.flush()
    debouncedSaveCheck.flush()
    if (uiState.inFlight || uiState.syncStatus === 'pending') {
      e.preventDefault()
      e.returnValue = ''
    }
  }
  onMounted(() => window.addEventListener('beforeunload', handleBeforeUnload))
  onBeforeUnmount(() => window.removeEventListener('beforeunload', handleBeforeUnload))

  return {
    uiState,
    getUnitLabel,
    getTaskTypeLabel,
    getAlarmLabel,
    clearError,
    handleCheckboxChange,
    handleValueChange,
  }
}
