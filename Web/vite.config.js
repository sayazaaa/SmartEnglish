import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
  base: '/SmartEnglish/',
  plugins: [vue()],
  resolve: {
    alias: { '@': path.resolve(__dirname, 'src') }
  },
  server: {
    proxy: {
      // 所有 /api 开头的请求都会被代理到后端
      '/api': {
        target: 'http://175.178.5.83:8080',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/api/, '')  // 去掉 /api 前缀
      }
    }
  }
});
