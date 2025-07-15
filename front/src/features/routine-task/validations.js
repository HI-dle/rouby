import {errorMessage} from "./constants.js";

export function validateForm(form, errors) {
  console.log(form)
  console.log(form.title)
  let isValid = true
  if (!form.title?.trim()) {
    errors.title = errorMessage.title.notNull
    isValid = false
  }

  if (!form.startDate) {
    errors.startDate = errorMessage.startDate.notNull
    isValid = false
  }

  if (!form.until) {
    errors.until = errorMessage.until.notNull
    isValid = false
  }

  if (!form.time) {
    errors.time = errorMessage.time.notNull
    isValid = false
  }

  return isValid
}
