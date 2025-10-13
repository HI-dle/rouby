import { getRoutines as getApi } from './api'
import { wrapApi } from '@/shared/utils/errorUtils'

export const getRoutines = wrapApi(async (fromDate, toDate) => {
  const res = await getApi({
    fromDate: fromDate,
    toDate: toDate
  })
  return res
}, {})

