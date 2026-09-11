/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
import axios from 'axios';
import { logout } from '@/utils/authState';

// 创建实例
const service = axios.create({
    baseURL: 'http://localhost:8080', // SpringBoot 端口
    timeout: 10000
});

// 请求拦截：自动带 Token
service.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            // Spring Security 标准格式: Bearer token
            config.headers['Authorization'] = token;
        }
        return config;
    },
    error => Promise.reject(error)
);

// 响应拦截
service.interceptors.response.use(
    response => {
        // 如果后端用 Result 包装，通常 code 200 表示成功
        // 这里直接返回 response.data，方便组件里解构
        return response.data;
    },
    error => {
        // 401 说明 Token 过期或无效
        if (error.response && error.response.status === 401) {
            logout(); // 清除本地状态
            // 可选：强制刷新或跳回登录页
        }
        return Promise.reject(error);
    }
);

export default service;