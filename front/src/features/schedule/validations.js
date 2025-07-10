import { errorMessage } from './constants'

export const validateForm = (form, errors) => {
  if (!form.title.trim()) {
    errors.title = errorMessage.title.notNull
    return false
  }
  if (!form.start) {
    errors.period = errorMessage.period.startNotNull
    return false
  }

  if (!form.end) {
    errors.period = errorMessage.period.endNotNull
    return false
  }

  if (form.start > form.end) {
    errors.period = errorMessage.period.invalidRange
    return false
  }

  if (form.routineStart && form.routineStart > form.end) {
    errors.routineStart = errorMessage.routineStart.reversed
    return false
  }
  return true
}
