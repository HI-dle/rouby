export function toMyPageResetPasswordPayload(form) {
  return {
    currentPassword: form.password,
    newPassword: form.newPassword,
    token: form.token?.trim()
  };
}
