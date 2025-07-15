import './styles/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { sessionStoragePlugin } from './stores/piniaSessionStorages'

const app = createApp(App)
const pinia = createPinia()
pinia.use(sessionStoragePlugin)

app.use(pinia)
app.use(router)

app.mount('#app')
