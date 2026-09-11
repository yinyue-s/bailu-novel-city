/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
import { ref } from 'vue';

// 这里的状态会响应式更新
export const isLoggedIn = ref(!!localStorage.getItem('token'));
// 新增：读取本地存储的用户名，如果没有则显示默认
export const username = ref(localStorage.getItem('username') || '书友');

export const login = (token, name) => {
    localStorage.setItem('token', token);
    // 存入用户名
    localStorage.setItem('username', name);

    isLoggedIn.value = true;
    username.value = name;
};

export const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');

    isLoggedIn.value = false;
    username.value = '';
};