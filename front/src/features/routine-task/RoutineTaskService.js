import {
  createRoutineTask as createApi,
  progressDailyTask as progressApi,
} from './api'

export const createRoutineTask = (payload) => {
  return createApi(payload)
}

export const progressDailyTask = (payload) => {
  return progressApi(payload)
}
