import axios from '@/api/axios' // 혹은 axios 직접 import

export async function getUserBasicInfo() {
  const res = await axios.get('/v1/users/basic-info')
  return res.data
}
