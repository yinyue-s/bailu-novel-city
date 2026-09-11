/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
// 从Vue核心库中导入创建应用实例的核心方法 createApp
import { createApp } from 'vue'

// 入项目的根组件 App.vue
// App.vue 通常包含全局布局（导航栏、底部栏）、路由出口 <router-view> 等核心结构
import App from './App.vue'

// 导入提前配置好的路由实例（来自 ./router 目录，配置了首页/书籍列表/登录页等路由规则）
// 路由实例用于实现白鹭文学城的页面跳转、路由守卫等功能
import router from './router'

// 用 Axios 发送请求（比如请求书籍数据、用户登录接口），可在此导入并全局挂载
import axios from 'axios'


// 创建 Vue 应用实例，传入根组件 App 作为应用的起点
// app 是当前应用的核心容器，所有插件/全局配置都基于这个实例
const app = createApp(App)

// 给应用实例挂载路由插件
// 挂载后才能在组件中使用 <router-link>、<router-view>、this.$router 等路由功能
app.use(router)

//全局挂载 Axios，方便所有组件直接通过 this.$axios 调用（无需重复导入）
app.config.globalProperties.$axios = axios

// 将创建好的 Vue 应用挂载到页面中 id 为 app 的 DOM 元素上（对应 public/index.html 中的 <div id="app"></div>）
// 执行 mount 后，Vue 会把 App.vue 的内容渲染到该 DOM 中，白鹭文学城应用正式在浏览器显示
// 注意：mount 必须放在最后（先挂载插件，再渲染页面），否则插件功能会失效
app.mount('#app')