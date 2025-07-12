import axios from '@/api/axios'

export const login = async ({ email, password }) => {
  const response = await axios.post('/v1/auth/login', {
    email,
    password,
  })

  return response.data.token // LoginResponse.token
}
