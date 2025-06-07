import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
//import store from './store'
import axios from 'axios';

// 1. baseURL 指向环境变量（带 /api 前缀 方便走代理）
// —— 这里判断一下，如果是 dev，就走 Vite 代理；否则走真实地址 ——
if (import.meta.env.DEV) {
    axios.defaults.baseURL = '/api';
} else {
    axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;
}

console.log('[Debug] axios.baseURL =', axios.defaults.baseURL);

axios.interceptors.request.use((cfg) => {
    const token = localStorage.getItem('admin_token');
    if (token) cfg.headers.Authorization = token;
    return cfg;
});

axios.interceptors.response.use(
    (res) => res,
    (err) => {
        if (err.response?.status === 401) {
            localStorage.removeItem('admin_token');
            router.replace('/login');
        }
        return Promise.reject(err);
    }
);


const app = createApp(App);

app.config.globalProperties.$axios = axios;

import elementPlus from 'element-plus'
import 'element-plus/dist/index.css';
app.use(elementPlus);
app.use(router);

app.mount('#app')

