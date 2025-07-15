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


