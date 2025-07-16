export function toSignupPayload(form) {
  return {
    email: form.email?.trim(),
    password: form.password
  };
}

export function toEmailVerificationPayload(email) {
  return {
    email: email?.trim()
  };
}

export function toVerifyCodePayload(email, code) {
  return {
    email: email?.trim(),
    code: code?.trim()
  };
}

export function toVerifyPasswordCodePayload(form) {
  console.log('dto', form)
  return {
    email: form.email?.trim(),
    token: form.token?.trim()
  };
}

export function toResetPasswordPayload(form) {
  return {
    email: form.email,
    newPassword: form.password,
    token: form.token?.trim()
  };
}

