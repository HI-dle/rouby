// '@/features/routine-task/RoutineTaskService.js'
import { createRoutineTask as createApi } from './api'

export const createRoutineTask = (payload) => {
  return createApi(payload)
}
