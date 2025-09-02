import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { version } from './package.json'
import * as path from 'node:path'
import { existsSync, cpSync } from 'node:fs'

function copyEnvFromSecret() {
  return {
    name: 'copy-env-from-secret',
    enforce: 'pre',
    apply: () => true,
    configResolved(config) {
      const mode = config.mode || 'development'
      const root = config.root
      const candidates = [
        path.join(root, '../rouby-secret', `.env.${mode}.front`),
        path.join(root, '../rouby-secret', `.env.front`),
      ]
      const target = path.join(root, `.env.${mode}`)
      const src = candidates.find((p) => existsSync(p))
      if (src) {
        cpSync(src, target)
        console.log(`[vite] copied ${path.basename(src)} -> .env.${mode}`)
      } else {
        console.warn(`[vite] no .env found in ./secret for mode=${mode}`)
      }
    },
  }
}

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools(), copyEnvFromSecret()],
  define: {
    'import.meta.env.VITE_APP_VERSION': JSON.stringify(version),
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
