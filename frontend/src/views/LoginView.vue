/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
/* ==========================================================================
   说明: 注册/登录页面
   - 对应原 login.html
   ========================================================================== */
<template>
 < <div class="login-body">
    <div class="login-container">
      <div class="login-card">
        
        <!-- Logo 横幅 -->
        <div class="logo-header">
          <div class="logo-bg">
             <img src="@/assets/images/白鹭文学城log.png"></img>
          </div>      
        </div>

        <!-- Tab 切换 -->
        <div class="login-tabs">
          <span 
            class="tab-item" 
            :class="{ active: activeTab === 'login' }" 
            @click="activeTab = 'login'"
          >
            登录
          </span>
          <span 
            class="tab-item" 
            :class="{ active: activeTab === 'register' }" 
            @click="activeTab = 'register'"
          >
            注册
          </span>
        </div>

        <!-- === 登录表单 === -->
        <div v-if="activeTab === 'login'" class="form-content">
          <div class="form-group">
            <label>用户名/手机号/邮箱</label>
            <div class="input-group">
              <input 
                type="text" 
                v-model="loginForm.username" 
                placeholder="请输入用户名/手机号/邮箱"
              >
            </div>
          </div>

          <div class="form-group">
            <label>密码</label>
            <div class="input-group">
              <input 
                type="password" 
                v-model="loginForm.password" 
                placeholder="请输入密码"
                @keyup.enter="handleLogin"
              >
            </div>
          </div>

          <div class="form-options">
            <div class="option-left">
                <a href="#" class="forgot-link">忘记密码?</a>
            </div>
            <label class="remember-me">
              <input type="checkbox" v-model="loginForm.remember">
              <span>记住我</span>
            </label>
          </div>

          <button class="submit-btn" @click="handleLogin">登录</button>
        </div>

        <!-- === 注册表单 === -->
        <div v-if="activeTab === 'register'" class="form-content">
          <div class="form-group">
            <label>用户名</label>
            <div class="input-group">
              <input type="text" v-model="registerForm.username" placeholder="请设置用户名">
            </div>
          </div>

          <div class="form-group">
             <label>邮箱</label>
             <div class="input-group">
               <input type="text" v-model="registerForm.email" placeholder="请输入邮箱">
             </div>
          </div>

          <div class="form-group">
            <label>手机号</label>
            <div class="input-group with-btn">
              <input type="text" v-model="registerForm.phone" placeholder="请输入手机号">
              <button class="verify-btn">获取验证码</button>
            </div>
          </div>

          <div class="form-group">
             <label>验证码</label>
             <div class="input-group">
               <input type="text" v-model="registerForm.code" placeholder="请输入验证码">
             </div>
          </div>

          <div class="form-group">
            <label>密码</label>
            <div class="input-group">
              <input type="password" v-model="registerForm.password" placeholder="请设置密码 (6-16位)">
            </div>
          </div>

          <div class="form-group">
            <label>角色</label>
            <div class="input-group">
              <select v-model="registerForm.role" class="role-select">
                <option value="" disabled>请选择角色</option>
                <option value="READER">读者</option>
                <option value="AUTHOR">作者</option>
                <option value="EDITOR">编辑</option>
              </select>
            </div>
          </div>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" v-model="registerForm.agreement">
              <span>我已阅读并同意 用户协议 和 隐私政策</span>
            </label>
          </div>

          <button class="submit-btn" @click="handleRegister">注册</button>
        </div>

        <!-- 底部其他方式  -->
        <div class="footer-area">
          <div class="divider">
            <span>其他登录方式</span>
          </div>
          <div class="social-icons">
            <div class="social-btn wechat"><i class="fab fa-weixin"></i></div>
            <div class="social-btn qq"><i class="fab fa-qq"></i></div>
            <div class="social-btn weibo"><i class="fab fa-weibo"></i></div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';
import { login as setAuthLogin } from '@/utils/authState'; 

const router = useRouter();
const activeTab = ref('login');
const rolePages = { 'READER': '/profile', 'AUTHOR': '/author', 'EDITOR': '/editor', 'ADMIN': '/admin' };
const loginForm = reactive({ username: '', password: '', remember: false });
const registerForm = reactive({ username: '', password: '', phone: '', role: '', agreement: false, email: '', code: '' });

const executeLogin = async (username, password, remember) => {
  try {
    const res = await request.post('/api/auth/login', { username, password });
    const data = res.data || res;
    const token = data.token || res.token;
    const role = data.role || res.role || (activeTab.value === 'register' ? registerForm.role : 'READER');
    const nickname = data.username || username;

    if (token) {
      const finalToken = token.startsWith('Bearer') ? token : `Bearer ${token}`;
      const storage = remember ? localStorage : sessionStorage;
      storage.setItem('token', finalToken);
      storage.setItem('role', role);
      setAuthLogin(finalToken, nickname);
      router.push(rolePages[role] || '/profile');
    } else {
      alert('登录失败');
    }
  } catch (error) {
    alert(error.response?.data?.message || '登录失败');
  }
};

const handleLogin = () => {
  if (!loginForm.username || !loginForm.password) return alert('请输入完整信息');
  executeLogin(loginForm.username, loginForm.password, loginForm.remember);
};

const handleRegister = async () => {
  if (!registerForm.username || !registerForm.password || !registerForm.role || !registerForm.agreement) return alert('请填写完整信息');
  try {
    const res = await request.post('/api/auth/register', registerForm);
    if (res.code === 200 || res.success) {
      await executeLogin(registerForm.username, registerForm.password, true);
    } else {
      alert(res.message || '注册失败');
    }
  } catch (error) {
    alert('注册异常');
  }
};
</script>

<style scoped>
@import '@/assets/styles/login.css';

</style>