import axios from '@/api/axios.js'

export const myPageResetPassword = (payload) => {
  return axios.patch('/v1/users/password/reset', payload)
}

export const myPageReadRoubySetting = () => {
  return axios.get('/v1/users/rouby-setting')
}

export const myPageUpdateRoubySetting = (payload) => {
  return axios.put('/v1/users/rouby-setting', payload)
}

export const myPageReadUserInfo = () => {
  return axios.get('/v1/users/user-info')
}

export const myPageUpdateUserInfo = (payload) => {
  return axios.patch('/v1/users/user-info', payload)
}
