import { toCreateSchedulePayload } from './dto'
import { createSchedule as createApi } from './api'

export const createSchedule = async (form) => {
  try {
    const payload = toCreateSchedulePayload(form)
    const res = await createApi(payload)
    const scheduleId = res.headers['location']?.split('/').filter(Boolean).pop()
    return scheduleId
  } catch (error) {
    // 에러 타입별 처리
    if (error.response?.status === 400) {
      throw new Error('입력 데이터가 올바르지 않습니다.')
    } else if (error.response?.status === 500) {
      throw new Error('서버 오류가 발생했습니다.')
    } else {
      throw new Error('저장에 실패하였습니다.')
    }
  }
}
