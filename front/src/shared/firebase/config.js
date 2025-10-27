import { getApp, getApps, initializeApp } from 'firebase/app'
import { getMessaging, onMessage } from 'firebase/messaging'
import { useToast } from '../composable/useToast'
import { safeNavigate } from '../utils/linkUtils'

const DEFAULT_ICON_PATH = '/assets/header_logo.svg'
const DEFAULT_BADGE_PATH = '/assets/header_logo.svg'

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
  try {
    globalThis.__firebaseApp__ = getApp()
  } catch (error) {
    globalThis.__firebaseApp__ = initializeApp(firebaseConfig)
  }
}
export const app = globalThis.__firebaseApp__
export const regSw = await ensureSw()

const toast = useToast()

const seen = new Map()
const DEDUPE_CH = 'noti-dedupe'
const MAX_SEEN = 1000
const TRIM_COUNT = 500

const bc = 'BroadcastChannel' in window ? new BroadcastChannel(DEDUPE_CH) : null

const markAndCheckSeen = (id) => {
  if (seen.has(id)) return true

  const now = Date.now()
  seen.set(id, now)

  if (seen.size > MAX_SEEN) {
    let i = 0
    for (const key of seen.keys()) {
      seen.delete(key)
      if (++i >= TRIM_COUNT) break
    }
  }

  if (bc) bc.postMessage({ id, ts: now })
  else navigator.serviceWorker?.controller?.postMessage({ id, ts: now })
  return false
}

// --- 탭 간 수신(BC 우선, SW 폴백) ---
const resolvePropagatedEvent = (e) => {
  const { id, ts } = e.data || {}
  if (id && typeof ts === 'number') {
    const prev = seen.get(id) ?? 0
    if (ts > prev) seen.set(id, ts)
  }
}

bc?.addEventListener('message', resolvePropagatedEvent)
navigator.serviceWorker?.addEventListener('message', resolvePropagatedEvent)

let isForegroundListenerRegistered = false
export const listenForeground = () => {
  if (!app || isForegroundListenerRegistered) {
    return
  }

  const messaging = getMessaging(app)
  onMessage(messaging, async (payload) => {
    const id = payload.data?.eventId
    if (id && markAndCheckSeen(id)) return

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
            DEFAULT_ICON_PATH,
          badge: DEFAULT_BADGE_PATH,
          data: { url },
          tag: id ? 'evt:' + id : (payload.data?.tag ?? 'rouby'),
          renotify: false,
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
      onClick: () => url && safeNavigate(url),
    })
  })
  isForegroundListenerRegistered = true
}
