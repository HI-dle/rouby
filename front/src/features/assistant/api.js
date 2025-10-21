import axios from '@/api/axios'

export const getBriefing = (date) => {
  return axios.get(`/v1/assistants/briefings/${date}`)
}

export const requestFeedback = (payload) => {
  return axios.post(`/v1/assistants/feedbacks`, payload)
}

export const getFeedbacks = (date) => {
  return axios.get(`/v1/assistants/feedbacks/daily/${date}`)
}
