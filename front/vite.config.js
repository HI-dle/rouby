import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { version } from './package.json'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  define: {
    'import.meta.env.VITE_APP_VERSION': JSON.stringify(version),
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
