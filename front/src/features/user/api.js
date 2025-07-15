import axios from '@/api/axios.js'

export const myPageResetPassword = (payload) => {
  return axios.patch('/v1/users/password/reset', payload)
}
