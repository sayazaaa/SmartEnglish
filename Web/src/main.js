import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
//import store from './store'
import axios from 'axios';

axios.defaults.baseURL = "/api";   // 只这一行

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

