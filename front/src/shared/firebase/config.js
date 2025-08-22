import { deleteApp, getApps, initializeApp } from 'firebase/app'

const firebaseConfig = {
  apiKey: 'AIzaSyDxNRABjbRUDDqQiplpwYxzp5TUu8Z_cUw',
  authDomain: 'rouby-b3e76.firebaseapp.com',
  projectId: 'rouby-b3e76',
  storageBucket: 'rouby-b3e76.firebasestorage.app',
  messagingSenderId: '344864966095',
  appId: '1:344864966095:web:1e7789e68bf68a93853eba',
}

export async function ensureSw() {
  if (!('serviceWorker' in navigator)) return
  let reg = await navigator.serviceWorker.getRegistration()
  if (!reg) {
    reg = await navigator.serviceWorker.register('/firebase-messaging-sw.js', {
      scope: '/',
    })
  }
  await navigator.serviceWorker.ready
  return reg
}

export const regSw = await ensureSw()
export const app =
  getApps().length === 0 ? initializeApp(firebaseConfig) : getApps()[0]
