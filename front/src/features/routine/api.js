import axios from '@/api/axios'

export const getRoutines = (params) => {
  if (!params || typeof params !== 'object') {
    throw new Error('유효하지 않은 params입니다.')
  }
  return axios.get('/v1/routine-task', {
    params: params,
  })
}
