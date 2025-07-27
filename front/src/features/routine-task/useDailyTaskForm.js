import { reactive } from 'vue'
import { wrapApi } from '@/shared/utils/errorUtils.js'
import { typeOptions, alarmOptions } from './constants'
import { progressDailyTask } from './RoutineTaskService.js'

export function useDailyTaskForm(task) {
  const uiState = reactive({
    displayValue: task.currentValue,
    isProcessing: false,
    errorMessage: '',
    fieldErrors: {},
    lastSavedValue: task.currentValue,
    syncStatus: 'synced', // 'synced', 'pending', 'failed'
  })

  const updateTaskProgressApi = wrapApi(progressDailyTask, {
    fallbackMessage: '저장에 실패했습니다. 다시 시도해주세요.',
  })

  // 단위 라벨
  const getUnitLabel = () => {
    switch (task.taskType) {
      case 'COUNT':
        return '회'
      case 'MINUTES':
        return '분'
      default:
        return ''
    }
  }

  // 타입 라벨
  const getTaskTypeLabel = () => {
    const typeOption = typeOptions.find(
      (option) => option.value === task.taskType,
    )
    return typeOption?.label || '알 수 없음'
  }

  // 알림 라벨
  const getAlarmLabel = () => {
    const alarmOption = alarmOptions.find(
      (option) => option.value === task.alarmOffsetMinutes,
    )
    return alarmOption?.label || '사용자 설정'
  }

  // 에러 메시지 제거
  const clearError = () => {
    uiState.errorMessage = ''
    uiState.fieldErrors = {}
  }

  // 옵티미스틱 업데이트 처리
  const handleOptimisticUpdate = async (newValue) => {
    // 1. 즉시 UI 업데이트 (옵티미스틱)
    uiState.displayValue = newValue
    uiState.syncStatus = 'pending'
    uiState.errorMessage = ''
    uiState.fieldErrors = {}

    try {
      const requestData = {
        id: task.id,
        routineTaskId: task.routineTaskId,
        currentValue: newValue,
        taskDate: task.date,
      }

      console.log('API 요청 데이터:', requestData)

      const result = await updateTaskProgressApi(requestData)

      task.id = result.id
      task.currentValue = newValue
      uiState.lastSavedValue = newValue
      uiState.syncStatus = 'synced'
      console.log('저장 완료:', result)
    } catch (error) {
      // 실패 시 롤백 및 에러 표시 (wrapApi가 처리한 에러)
      uiState.displayValue = uiState.lastSavedValue
      uiState.syncStatus = 'failed'

      // 필드별 에러가 있으면 필드 에러로, 없으면 전역 에러로 표시
      if (error.fieldErrors) {
        uiState.fieldErrors = error.fieldErrors
      } else {
        uiState.errorMessage = error.message
      }

      console.error('저장 실패:', error)
    }
  }

  // 체크박스 변경 처리 (즉시 UI 반영, 저장은 지연)
  const handleCheckboxChange = (event) => {
    // 저장 중이면 무시
    if (uiState.syncStatus === 'pending') {
      return
    }

    const newValue = event.target.checked ? 1 : 0
    console.log('체크박스 클릭 - UI만 즉시 반영:', newValue)

    // 즉시 UI만 반영 (저장은 안함)
    uiState.displayValue = newValue

    // 값이 변경되었음을 표시
    if (newValue !== uiState.lastSavedValue) {
      uiState.syncStatus = 'pending'
    } else {
      uiState.syncStatus = 'synced'
    }
  }

  // 체크박스 영역에서 마우스가 벗어날 때 저장
  const handleCheckboxBlur = () => {
    console.log(
      '체크박스 마우스 아웃 - 저장 시도:',
      uiState.displayValue,
      'vs',
      uiState.lastSavedValue,
    )

    if (
      uiState.displayValue !== uiState.lastSavedValue &&
      uiState.syncStatus === 'pending'
    ) {
      console.log('체크박스 저장 실행!')
      handleOptimisticUpdate(uiState.displayValue).catch()
    }
  }

  // 숫자 값 변경 처리 (즉시 UI 반영, 저장은 지연)
  const handleValueChange = (newValue) => {
    if (newValue < 0 || newValue > 999) {
      return
    }

    uiState.displayValue = newValue
    // 값이 변경되었음을 표시
    if (newValue !== uiState.lastSavedValue) {
      uiState.syncStatus = 'pending'
    } else {
      uiState.syncStatus = 'synced'
    }
  }

  // 숫자 입력 영역에서 마우스가 벗어날 때 저장
  const handleNumberInputBlur = () => {
    if (
      uiState.displayValue !== uiState.lastSavedValue &&
      uiState.syncStatus === 'pending'
    ) {
      handleOptimisticUpdate(uiState.displayValue).then()
    }
  }

  return {
    uiState,
    getUnitLabel,
    getTaskTypeLabel,
    getAlarmLabel,
    clearError,
    handleCheckboxChange,
    handleCheckboxBlur,
    handleValueChange,
    handleNumberInputBlur,
  }
}
