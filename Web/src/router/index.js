
import {createRouter, createWebHashHistory} from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import WordSetManager from "@/views/WordSetManager.vue";
import WordManager from "@/views/WordManager.vue";
import ArticleManager from "@/views/ArticleManager.vue";
import FeedBack from "@/views/FeedBack.vue";
import Statistic from "@/views/Statistic.vue";


const routes = [
   {
        path: '/',
        redirect:'/wordsetmanager'
    },

    {
        path: '/login',
        name: 'Login',
        component: Login
    },

   {
        path: '/wordsetmanager',
        name:'WordSetManager',
        component:WordSetManager
    },

    {
        path: '/wordmanager',
        name:'WordManager',
        component: WordManager
    },

    {
        path:'/articlemanager',
        name:'ArticleManager',
        component: ArticleManager
    },

    {
        path: '/feedback',
        name:'Feedback',
        component: FeedBack
    },

    {
        path:'/statistic',
        name:'Statistic',
        component: Statistic
    }
]

const router = createRouter({
    routes,
    history:createWebHashHistory()
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem("admin_token");
    // 如果去的是 /login，不做拦截
    if (to.path === "/login") {
        next();
        return;
    }
    // 如果没有 token，就跳到登录页
    if (!token) {
        next({ path: "/login" });
        return;
    }
    // 否则正常访问
    next();
});

export default router
