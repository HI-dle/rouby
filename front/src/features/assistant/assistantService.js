import {
  getBriefing as getBriefingApi,
  getFeedbacks as getFeedbacksApi,
  requestFeedback as requestFeedbackApi,
} from './api'
import { wrapApi } from '@/shared/utils/errorUtils'

export const getBriefing = wrapApi(async (date) => {
  if (!date) {
    throw new Error('date is required')
  }
  const res = await getBriefingApi(date)
  return res.data
})

export const requestFeedback = wrapApi(async (payload) => {
  const res = await requestFeedbackApi(payload)
  return res.data
})

export const getFeedbacks = wrapApi(async (date) => {
  const res = await getFeedbacksApi(date)
  return res.data
})
