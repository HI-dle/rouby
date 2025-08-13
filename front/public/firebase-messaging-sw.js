importScripts(
  'https://www.gstatic.com/firebasejs/12.0.0/firebase-app-compat.js',
)
importScripts(
  'https://www.gstatic.com/firebasejs/12.0.0/firebase-messaging-compat.js',
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

self.addEventListener('notificationclick', (event) => {
  console.log('알림 클릭 이벤트 발생:', event)
  event.notification.close()

  const urlToNavigate = event.notification.data.url
  if (!urlToNavigate) return

  event.waitUntil(
    (async () => {
      const clientsList = await clients.matchAll({
        type: 'window',
        includeUncontrolled: true,
      })

      const sameOriginClient = clientsList.find((client) =>
        client.url.startsWith(self.location.origin),
      )

      if (sameOriginClient) {
        await sameOriginClient.focus()
        await sameOriginClient.navigate(urlToNavigate)
      } else {
        await clients.openWindow(urlToNavigate)
      }
    })(),
  )
})
