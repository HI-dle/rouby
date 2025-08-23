export function toRegisterUserDevice(token, deviceInfo) {
  return {
    tokenProvider: 'FCM',
    deviceToken: token,
    ...deviceInfo,
  }
}
