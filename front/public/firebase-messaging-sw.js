self.addEventListener('install', () => self.skipWaiting())
self.addEventListener('activate', (event) => {
  event.waitUntil(self.clients.claim())
})

importScripts(
  'https://www.gstatic.com/firebasejs/12.2.1/firebase-app-compat.js',
)
importScripts(
  'https://www.gstatic.com/firebasejs/12.2.1/firebase-messaging-compat.js',
)
const firebaseConfig = {
  apiKey: 'AIzaSyDxNRABjbRUDDqQiplpwYxzp5TUu8Z_cUw',
  authDomain: 'rouby-b3e76.firebaseapp.com',
  projectId: 'rouby-b3e76',
  storageBucket: 'rouby-b3e76.firebasestorage.app',
  messagingSenderId: '344864966095',
  appId: '1:344864966095:web:1e7789e68bf68a93853eba',
}

const app = firebase.initializeApp(firebaseConfig)
const messaging = firebase.messaging()

const DEDUPE_CH = 'noti-dedupe'
const STORE = 'seen'
const RETENTION_MS = 24 * 60 * 60_000 // 24 시간 보존
const PRUNE_LIMIT = 1000

// --- 알림 이벤트 중복 아이디 저장용 IDB 유틸 --
function idbOpen() {
  return new Promise((res, rej) => {
    const r = indexedDB.open(DEDUPE_CH, 1)
    r.onupgradeneeded = () => {
      try {
        r.result.createObjectStore(STORE) // key=id, value=ts(ms)
      } catch (e) {
        rej(e)
        return
      }
    }
    r.onsuccess = () => res(r.result)
    r.onerror = () => rej(r.error)
    r.onblocked = () => {
      console.warn('IndexedDB update blocked. Closing other tabs might help.')
    }
  })
}

async function idbGet(id) {
  const db = await idbOpen()

  return new Promise((res, rej) => {
    const tx = db.transaction(STORE, 'readonly')
    const rq = tx.objectStore(STORE).get(id)
    rq.onsuccess = () => res(rq.result ?? null)
    rq.onerror = () => rej(rq.error)
  })
}

async function idbPut(id, ts) {
  const db = await idbOpen()

  return new Promise((res, rej) => {
    const tx = db.transaction(STORE, 'readwrite')
    tx.objectStore(STORE).put(ts, id)
    tx.oncomplete = () => res()
    tx.onerror = () => rej(tx.error)
  })
}

async function idbPrune(cutoff, limit = PRUNE_LIMIT) {
  const db = await idbOpen()

  return new Promise((res, rej) => {
    const tx = db.transaction(STORE, 'readwrite')
    const store = tx.objectStore(STORE)
    const cur = store.openCursor()

    let n = 0
    cur.onsuccess = () => {
      const c = cur.result
      if (!c || n >= limit) return
      const ts = c.value
      if (typeof ts === 'number' && ts < cutoff) {
        c.delete()
        n++
      }
      c.continue()
    }
    tx.oncomplete = () => res(n)
    tx.onerror = () => rej(tx.error)
  })
}

// --- 알림 이벤트 전파 ---
const bc = 'BroadcastChannel' in self ? new BroadcastChannel(DEDUPE_CH) : null
const broadcastToPages = async (msg) => {
  if (bc) {
    bc.postMessage(msg)
  } else {
    const cs = await self.clients.matchAll({
      type: 'window',
      includeUncontrolled: true,
    })
    cs.forEach((c) => c.postMessage(msg))
  }
}

const resolvePropagatedEvent = async (e) => {
  const { id, ts } = e.data || {}

  if (id && typeof ts === 'number') {
    const storedTs = await idbGet(id)
    if (storedTs && typeof storedTs === 'number') return

    idbPut(id, ts).catch(() => {})
  }
}
bc && bc.addEventListener('message', resolvePropagatedEvent)
self.addEventListener('message', resolvePropagatedEvent)

// --- 알림 이벤트 중복 체크 + 기록 ---
async function markAndCheckSeen(id) {
  if (!id) return false

  const ts = await idbGet(id)
  if (ts && typeof ts === 'number') return true

  const now = Date.now()
  idbPut(id, now).catch(() => {})
  if (Math.random() < 0.1) idbPrune(now - RETENTION_MS).catch(() => {})

  broadcastToPages({ id, ts: now })
  return false
}

// --- 알림 이벤트 수신 ---
messaging.onBackgroundMessage(async (payload) => {
  if (payload && payload.notification) return

  const data = payload?.data || {}
  const id = data.eventId
  if (id && (await markAndCheckSeen(id))) return

  const title = data.title || '알림'
  const options = {
    body: data.body || '',
    icon: data.icon || '/assets/header_logo.svg',
    data: { url: data.url || '/' },
    tag: id ? `evt:${id}` : (payload.data?.tag ?? 'rouby'),
  }

  self.registration.showNotification(title, options)
  if (id) await broadcastToPages({ id, ts: Date.now() })
})

self.addEventListener('notificationclick', (event) => {
  event.notification.close()

  const urlToNavigate = event.notification.data.url
  if (!urlToNavigate) return

  const u = new URL(urlToNavigate, location.origin)
  if (!/^https?:$/.test(u.protocol)) return

  const allowed = [location.origin]
  if (!allowed.includes(u.origin)) return

  event.waitUntil(self.clients.openWindow(u.href))
})
