import { getApps, initializeApp } from 'firebase/app'
import { getMessaging, onMessage } from 'firebase/messaging'
import { useToast } from '../composable/useToast'

const firebaseConfig = {
  apiKey: 'AIzaSyDxNRABjbRUDDqQiplpwYxzp5TUu8Z_cUw',
  authDomain: 'rouby-b3e76.firebaseapp.com',
  projectId: 'rouby-b3e76',
  storageBucket: 'rouby-b3e76.firebasestorage.app',
  messagingSenderId: '344864966095',
  appId: '1:344864966095:web:1e7789e68bf68a93853eba',
}

export async function ensureSw() {
  if (!('serviceWorker' in navigator)) {
    return Promise.reject(new Error('ServiceWorker not supported'))
  }
  if (!globalThis.__swRegPromise__) {
    globalThis.__swRegPromise__ = (async () => {
      // 이미 등록돼 있으면 재사용
      const existing = await navigator.serviceWorker.getRegistration()
      if (existing) {
        const u =
          (existing.active || existing.waiting || existing.installing)
            ?.scriptURL || ''
        if (u.endsWith('/firebase-messaging-sw.js')) {
          return existing
        }
        await existing.unregister()
      }

      // 없으면 새로 등록
      const reg = await navigator.serviceWorker.register(
        '/firebase-messaging-sw.js',
        {
          scope: '/',
          updateViaCache: 'none',
        },
      )
      await navigator.serviceWorker.ready
      return reg
    })()
  }
  return globalThis.__swRegPromise__
}

if (!globalThis.__firebaseApp__) {
  globalThis.__firebaseApp__ = getApps().length
    ? getApp()
    : initializeApp(firebaseConfig)
}
export const app = globalThis.__firebaseApp__
export const regSw = await ensureSw()
const toast = useToast()

let isForegroundListenerRegistered = false
export const listenForeground = () => {
  if (!app || isForegroundListenerRegistered) {
    return
  }
  const messaging = getMessaging(app)
  onMessage(messaging, async (payload) => {
    const title = payload.notification?.title ?? payload.data?.title ?? '알림'
    const body = payload.notification?.body ?? payload.data?.body ?? ''
    const url = payload.fcmOptions?.link ?? payload.data?.url ?? '/'

    if ('Notification' in window && Notification.permission === 'granted') {
      const reg = await navigator.serviceWorker.getRegistration()

      if (reg) {
        await reg.showNotification(title, {
          body,
          icon:
            payload.notification?.icon ||
            payload.data?.icon ||
            '/assets/header_logo.svg',
          badge: '/assets/header_logo.svg',
          data: { url },
          tag: payload.data?.tag ?? 'rouby',
          renotify: true,
          requireInteraction: false,
        })
        return
      }
    }

    toast.show({
      title,
      message: body || title,
      variant: 'notification',
      duration: 8000,
      onClick: () => {
        if (url) window.location.href = url
      },
    })
  })
  isForegroundListenerRegistered = true
}
