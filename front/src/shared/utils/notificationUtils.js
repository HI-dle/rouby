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
  })
}
export const removeDeviceToken = async () => {
  if (!app || !regSw) {
    console.error('Firebase 앱 또는 서비스 워커가 초기화되지 않았습니다.')
    return
  }
  const messaging = getMessaging(app)
  try {
    const deleted = await deleteToken(messaging, {
      serviceWorkerRegistration: regSw,
    })
  } catch (error) {
    console.error('FCM 토큰 삭제 중 오류 발생:', error)
  }
}

let isForegroundListenerRegistered = false

export const listenForeground = () => {
  if (!app || isForegroundListenerRegistered) {
    return
  }
  const messaging = getMessaging(app)
  onMessage(messaging, (payload) => {
    console.log('Message received. ', payload)
    alert(payload.data.message)
  })
  isForegroundListenerRegistered = true
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
  if (granted) {
    listenForeground()
    const token = await getDeviceToken()
    console.log('FCM 토큰:', token)
  } else {
    console.warn('알림 권한이 없습니다. 기존 토큰을 삭제합니다.')
    await removeDeviceToken()
  }
}
