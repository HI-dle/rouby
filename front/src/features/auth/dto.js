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

export function toVerifyPasswordCodePayload(code) {
  return {
    code: code?.trim()
  };
}

export function toResetPasswordPayload(form) {
  return {
    userId: form.userId,
    password: form.password
  };
}

