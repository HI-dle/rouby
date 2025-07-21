import axios from '@/api/axios'

export const createRoutineTask = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효한 payload가 필요합니다.')
  }
  return axios.post('/v1/routine-task', payload)
    .catch(error => {
      console.error('루틴 태스크 생성 실패:', error)
      throw error
    })
}
