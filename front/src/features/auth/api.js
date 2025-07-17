import axios from '@/api/axios'

export const requestEmailVerification = (payload) => {
  return axios.post('/v1/users/email-verification/request', payload)
}

export const verifyEmail = (payload) => {
  return axios.post('/v1/users/email-verification/verify', payload)
}

export const signup = (payload, token) => {
  return axios.post('/v1/users', payload, {
    headers: {
      Authorization: `${token}`,
    }
  })
}

export const findPassword = (payload) => {
  return axios.post('/v1/users/password/find', payload)
}

export const resetPassword = (payload) => {
  return axios.patch('/v1/users/password/reset/token', payload)
}

export const verificationPasswordCode = (payload) => {
  return axios.get('/v1/users/password/reset/validate', {
    params: {
      email: payload.email,
      token: payload.token
    }
  })
}
