import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
//import store from './store'
import axios from 'axios';

axios.defaults.baseURL = "175.178.5.83:8080";

// 1. 请求拦截：每次请求自动带上 token
axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("admin_token");
        if (token) {
            config.headers.Authorization = token;//`Bearer ${token}`
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 2. 响应拦截：如果 401 未授权，可直接清除 token 并跳转登录
axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            // Token 过期或无效
            localStorage.removeItem("admin_token");
            router.replace("/login");
        }
        return Promise.reject(error);
    }
);

const app = createApp(App);

import elementPlus from 'element-plus'
import 'element-plus/dist/index.css';
app.use(elementPlus);
app.use(router);

app.mount('#app')

