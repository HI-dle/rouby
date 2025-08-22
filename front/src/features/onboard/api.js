import axios from '@/api/axios'

export const registerUserDevice = (payload) => {
  return axios.post('/v1/users/devices', payload)
}
