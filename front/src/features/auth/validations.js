import { errorMessages, passwordRequirements } from './constants.js'
import axios from '@/api/axios.js'

export function validateEmail(email) {
  if (!email?.trim()) {
    return errorMessages.email.required;
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email)) {
    return errorMessages.email.invalid;
  }

  return null
}

export function validatePassword(password) {
  if (!password?.trim()) {
    return errorMessages.password.required;
  }

  if (password.length < passwordRequirements.minLength) {
    return errorMessages.password.minLength;
  }

  if (password.length > passwordRequirements.maxLength) {
    return errorMessages.password.maxLength;
  }

  if (!passwordRequirements.pattern.test(password)) {
    return errorMessages.password.pattern;
  }

  return null;
}

export function validatePasswordConfirm(password, passwordConfirm) {
  if (!passwordConfirm?.trim()) {
    return errorMessages.password.required;
  }

  if (password !== passwordConfirm) {
    return errorMessages.password.mismatch;
  }

  return null;
}

export function validateSignupForm(form, errors) {
  let isValid = true;

  const emailError = validateEmail(form.email)
  if (emailError) {
    errors.email = emailError;
    isValid = false;
  }

  const passwordError = validatePassword(form.password)
  if (passwordError) {
    errors.password = passwordError;
    isValid = false;
  }

  const passwordConfirmError = validatePasswordConfirm(form.password, form.passwordConfirm)
  if (passwordConfirmError) {
    errors.passwordConfirm = passwordConfirmError;
    isValid = false;
  }

  return isValid;
}

export function validateEmailForm(form, errors) {
  let isValid = true;

  const emailError = validateEmail(form.email)
  if (emailError) {
    errors.email = emailError;
    isValid = false;
  }

  return isValid;
}
