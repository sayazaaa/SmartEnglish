import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
//import store from './store'

const app = createApp(App);

import elementPlus from 'element-plus'
import 'element-plus/dist/index.css';
app.use(elementPlus);
app.use(router);

app.mount('#app')

