/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
import axios from 'axios';

// 根据实际后端地址修改
const API_BASE_URL = 'http://localhost:8080';

// 请求超时时间（5 秒），如果 5 秒内后端没响应，直接触发错误
const api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 5000
});

// 请求拦截器：自动携带 token
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`; // 或 config.headers.token = token
    }
    return config;
});

export const userApi = {
    login: (data) => api.post('/user/login', data),
    register: (data) => api.post('/user/register', data),
    getProfile: () => api.get('/user/profile'),
    updateProfile: (data) => api.post('/user/update', data),
    logout: () => api.post('/user/logout')
};

export const bookApi = {
    getHomeData: () => api.get('/home/data'), // 对应 index.html 数据
    getBookshelf: () => api.get('/bookshelf/list'),
    getCategories: () => api.get('/category/list')
};

export const forumApi = {
    getTopics: () => api.get('/forum/topics'),
    getCreateTopic: (data) => api.post('/forum/create', data)
};

export default api;
