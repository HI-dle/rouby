import Bowser from 'bowser'
import {
  getMessaging,
  getToken,
  onMessage,
  deleteToken,
} from 'firebase/messaging'
import { app, regSw } from '../firebase/config'

export function extractDeviceInfo() {
  const parser = Bowser.getParser(window.navigator.userAgent)

  const os = parser.getOS()
  const browser = parser.getBrowserName()
  const browserVersion = parser.getBrowserVersion()
  const platform = parser.getPlatformType()

  return {
    appType: 'WEB',
    appVersion: import.meta.env.VITE_APP_VERSION ?? 'unknown',
    deviceType: platform?.toUpperCase(),
    os: `${os.name} ${os.version}`,
    browser: `${browser} ${browserVersion}`,
    userAgent: navigator.userAgent,
  }
}

export const getDeviceToken = async () => {
  if (!app || !regSw) {
    console.error('Firebase 앱 또는 서비스 워커가 초기화되지 않았습니다.')
    return null
  }
  const messaging = getMessaging(app)

  return await getToken(messaging, {
    serviceWorkerRegistration: regSw,
    vapidKey: import.meta.env.VITE_FCM_VAPID_KEY,
  })
}

export const removeDeviceToken = async () => {
  if (!app) {
    console.error('Firebase 앱 또는 서비스 워커가 초기화되지 않았습니다.')
    return
  }
  const messaging = getMessaging(app)
  try {
    await deleteToken(messaging)
  } catch (error) {
    console.error('FCM 토큰 삭제 중 오류 발생:', error)
  }
}

const requestNotificationPermission = async () => {
  if (!app || !regSw) {
    console.error('Firebase 앱 또는 서비스 워커가 초기화되지 않았습니다.')
    return
  }
  if (!('Notification' in window)) return false
  if (Notification.permission === 'granted') return true

  const permission = await Notification.requestPermission()
  return permission === 'granted'
}

export const requestPermissionAndInitFCM = async () => {
  const granted = await requestNotificationPermission()
  if (!granted) {
    console.warn('알림 권한이 없습니다. 기존 토큰을 삭제합니다.')
    await removeDeviceToken()
  }
}
