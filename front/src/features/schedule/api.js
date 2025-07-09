import axios from '@/api/axios'

export const createSchedule = (payload) => {
  if (!payload || typeof payload !== 'object') {
    throw new Error('유효하지 않은 payload입니다.')
  }
  return axios.post('/v1/schedules', payload)
}
