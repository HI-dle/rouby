import axios from '@/api/axios'

export const createRoutineTask = (payload) => {
  return axios.post('/v1/routine-task', payload)
}
