import axios from '@/api/axios'

export const createSchedule = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효하지 않은 payload입니다.')
  }
  return axios.post('/v1/schedules', payload)
}

export const getSchedules = (params) => {
  if (!params || typeof params !== 'object') {
    throw new Error('유효하지 않은 params입니다.')
  }
  return axios.get('/v1/schedules', {
    params: params,
  })
}

export const updateSchedule = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효하지 않은 payload입니다.')
  }
  return axios.put('/v1/schedules', payload)
}

export const deleteSchedule = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효하지 않은 payload입니다.')
  }
  // 단일 일정 삭제
  return axios.patch('/v1/schedules', payload, {
    headers: { 'Content-Type': 'application/json' },
  })
}

export const deleteSchedulesStartingFrom = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효하지 않은 payload입니다.')
  }
  // 이후 일정 삭제
  return axios.patch('/v1/schedules/from', payload, {
    headers: { 'Content-Type': 'application/json' },
  })
}
