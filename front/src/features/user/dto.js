export function toMyPageResetPasswordPayload(form) {
  return {
    currentPassword: form.password,
    newPassword: form.newPassword,
    token: form.token?.trim()
  };
}

export function toMyPageRoubySettingPayload(form) {
  return {
    communicationTone: form.communicationTone,
    notificationSettings: form.notificationSettings,
  };
}

export function toMyPageUserInfoPayload(form) {
  return {
    nickname: form.nickname,
    healthStatusKeywords: form.healthStatusKeywords,
    profileKeywords: form.profileKeywords,
    dailyStartTime: form.dailyStartTime,
    dailyEndTime: form.dailyEndTime,
  };
}
