import axios from '@/api/axios'

export const getBriefing = (date) => {
  return axios.get(`/v1/assistants/briefings/${date}`, )
}
