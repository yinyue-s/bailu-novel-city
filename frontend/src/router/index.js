/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
/**
    * path：路径
    * name：路由名称
    * component：路径对应的页面组件
    * meta：路由元信息
    */
import { createRouter, createWebHistory } from 'vue-router'

// 1. 静态导入：首页通常直接导入，保证加载速度
// 注意：这里全部改成 ../views/ 开头，坚决不用 @
import HomeView from '../views/HomeView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // --- 首页 ---
        {
            path: '/',
            redirect: '/home' // 访问根域名时自动跳到 /home
        },
        {
            path: '/home',
            name: 'home',
            component: HomeView
        },

        // --- 登录页 ---
        {
            path: '/login',
            name: 'login',
            // 下面这种写法叫“懒加载”，路径也要改成 ../views/
            component: () => import('../views/LoginView.vue'),
            meta: {
                hideNavbar: true, // 标记这里需要隐藏导航栏
                hideFooter: true  // 页脚隐藏
            }
        },


        // --- 个人书架 ---
        {
            path: '/bookshelf',
            name: 'bookshelf',
            component: () => import('../views/BookshelfView.vue'),
            meta: { requiresAuth: true } // 标记需要登录才能看
        },

        // --- 论坛 ---
        {
            path: '/forum',
            name: 'forum',
            component: () => import('../views/ForumView.vue')
        },

        // --- 个人信息/我的 ---
        {
            path: '/profile',
            name: 'profile',
            component: () => import('../views/ProfileView.vue'),
            meta: { requiresAuth: true }
        }
    ]
})

export default router