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
    config(userConfig, env) {
      const mode = env?.mode || 'development'
      const root = userConfig?.root
        ? path.resolve(userConfig.root)
        : process.cwd()
      const candidates = [
        path.join(root, '../rouby-secret', `.env.${mode}.front`),
        path.join(root, '../rouby-secret', `.env.front`),
      ]
      const target = path.join(root, `.env.${mode}`)
      const src = candidates.find((p) => existsSync(p))
      if (src) {
        if (existsSync(target)) {
          console.log(
            `[vite] skip copy: ${path.basename(target)} already exists`,
          )
        } else {
          cpSync(src, target, { force: false, errorOnExist: true })
          console.log(`[vite] copied ${path.basename(src)} -> .env.${mode}`)
        }
      } else {
        console.warn(
          `[vite] no env file found in ../rouby-secret for mode=${mode}`,
        )
      }
      return null
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
