import axios from '@/api/axios'

export const createSchedule = (payload) => {
  return axios.post('/v1/routine-task', payload)
}
