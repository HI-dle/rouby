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

messaging.onBackgroundMessage((payload) => {
  if (payload && payload.notification) {
    return
  }

  const data = payload?.data || {}
  const title = data.title || '알림'
  const options = {
    body: data.body || '',
    icon: data.icon || '/assets/header_logo.svg',
    data: { url: data.url || '/' },
  }

  self.registration.showNotification(title, options)
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
