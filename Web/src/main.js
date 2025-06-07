import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
//import store from './store'
import axios from 'axios';

// 1. baseURL 指向环境变量（带 /api 前缀 方便走代理）
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL + '/api';

// 2. 每次请求自动带上 token
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('admin_token');
    if (token) config.headers.Authorization = token;
    return config;
});

// 3. 如果 401，清除 token 并跳登录
axios.interceptors.response.use(
    res => res,
    err => {
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

