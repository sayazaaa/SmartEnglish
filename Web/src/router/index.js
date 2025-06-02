
import {createRouter, createWebHashHistory} from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import WordSetManager from "@/views/WordSetManager.vue";
import WordManager from "@/views/WordManager.vue";
import ArticleManager from "@/views/ArticleManager.vue";
import FeedBack from "@/views/FeedBack.vue";


const routes = [
   {
        path: '/',
        redirect:'/wordsetmanager'
    },

    /*{
        path: '/',
        name: 'Login',
        component: Login
    },*/

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
    }
]

const router = createRouter({
    routes,
    history:createWebHashHistory()
})

export default router
