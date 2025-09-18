import { getBriefing as getApi } from './api'
import { wrapApi } from '@/shared/utils/errorUtils'

export const getBriefing = wrapApi(async (date) => {
  if (!date) {
    throw new Error('date is required')
  }
  const res = await getApi(date)
  return res.data
})
