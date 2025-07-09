import './styles/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import { getStoredToken } from '@/features/auth/storeToken';
import axios from '@/api/axios';

// 앱 시작 시 토큰이 있으면 기본 헤더에 설정
const token = getStoredToken();
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
