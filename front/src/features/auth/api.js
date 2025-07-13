import axios from '@/api/axios'

export const requestEmailVerification = (payload) => {
  return axios.post('/v1/users/email-verification/request', payload)
}

export const verifyEmail = (payload) => {
  return axios.post('/v1/users/email-verification/verify', payload)
}

export const signup = (payload) => {
  return axios.post('/v1/users', payload)
}
