import axios from '@/api/axios'

export const createRoutineTask = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효한 payload가 필요합니다.')
  }
  return axios.post('/v1/routine-task', payload).catch((error) => {
    console.error('루틴 태스크 생성 실패:', error)
    throw error
  })
}

export const progressDailyTask = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효한 payload가 필요합니다.')
  }

  return axios
    .post('/v1/daily-task/progress', payload)
    .then((response) => response.data)
    .catch((error) => {
      console.error('데일리 태스크 업데이트 실패:', error)
      throw error
    })
}

export const getRoutines = (params) => {
  if (!params || typeof params !== 'object') {
    throw new Error('유효하지 않은 params입니다.')
  }
  return axios.get('/v1/routine-task', {
    params: params,
  })
}
