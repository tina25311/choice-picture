import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-i18n': ['vue-i18n']
        }
      }
    },
    // 确保多语言相关代码不被移除
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
        // 保留 vue-i18n 相关代码
        pure_funcs: ['console.log', 'console.info', 'console.debug']
      }
    }
  },
  define: {
    __VUE_I18N_FULL_INSTALL__: true,
    __VUE_I18N_LEGACY_API__: true,
    __INTLIFY_PROD_DEVTOOLS__: false,
    // 确保多语言消息不被移除
    __VUE_I18N_PROD_DEVTOOLS__: false
  },
  optimizeDeps: {
    include: ['vue-i18n']
  }
})
