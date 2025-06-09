import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
  base: '/SmartEnglish/',
  plugins: [vue()],
  resolve: { alias: { '@': path.resolve(__dirname, 'src') } },
  server: {
    proxy: {
      '/api': {
        target: 'https://api.sayaz.site',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})