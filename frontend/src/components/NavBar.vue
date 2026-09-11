<template>
  <nav class="navbar">
    <div class="nav-container">
      
      <!-- 左侧 Logo + 标题 (纯CSS/图标实现) -->
      <div class="nav-left">
        <!-- 羽毛图标 -->
        <i class="fas fa-feather-alt logo-icon"></i>
        <!-- 文字标题 -->
        <span class="brand-name">白鹭文学城</span>
      </div>

      <!-- 右侧导航区域 -->
      <div class="nav-right">
        
        <!-- 1. 中间菜单链接 -->
        <div class="nav-links">
          <router-link to="/" exact-active-class="active">首页</router-link>
          <router-link to="/forum" active-class="active">论坛</router-link>
          <router-link to="/bookshelf" active-class="active">个人书架</router-link>
          <router-link to="/profile" active-class="active">我的信息</router-link>
        </div>

        <!-- 2. 最右侧动态区域 -->
        <div class="nav-action">
          
          <!-- 情况A: 未登录 -->
          <div v-if="!isLoggedIn">
            <router-link to="/login" class="login-link">登录 / 注册</router-link>
          </div>

          <!-- 情况B: 已登录，在【我的信息】页 -> 显示红色退出按钮 -->
          <div v-else-if="isProfilePage">
            <button class="logout-btn" @click="handleLogout">
              <i class="fas fa-sign-out-alt"></i> 退出登录
            </button>
          </div>

          <!-- 情况C: 已登录，在其他页 -> 显示头像昵称 -->
          <div v-else class="user-profile" @click="goToProfile">
            <i class="fas fa-user-circle avatar-icon"></i>
            <span class="nickname">{{ currentName }}</span>
          </div>

        </div>

      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { isLoggedIn, username, logout } from '@/utils/authState';

const router = useRouter();
const route = useRoute();

// 判断是否在个人信息页
const isProfilePage = computed(() => route.path === '/profile');
const currentName = computed(() => username.value || '书友');

// 跳转到个人中心
const goToProfile = () => {
  router.push('/profile');
};

// 退出登录
const handleLogout = () => {
  if(confirm('确定要退出登录吗？')) {
    logout();
    router.push('/login');
  }
};
</script>

<style scoped>
/* === 导航栏容器 === */
.navbar {
  width: 100%;
  height: 64px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0; /* 极淡的分割线 */
  display: flex;
  justify-content: center;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-container {
  width: 100%;
  max-width: 1200px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* === 左侧 Logo (图二样式复刻) === */
.nav-left {
  display: flex;
  align-items: center;
  gap: 12px; /* 图标和文字的间距 */
  user-select: none;
}

/* 羽毛图标样式 */
.logo-icon {
  font-size: 28px;       /* 图标要大 */
  color: #879bad;        /* 核心颜色：莫兰迪蓝灰 (匹配图二) */
  transform: rotate(-0deg); /* 如果觉得角度不对，可以微调，例如 rotate(-10deg) */
}

/* 品牌名称样式 */
.brand-name {
  font-size: 22px;
  font-weight: 800;      /* 加粗，匹配图二的粗黑体 */
  color: #333333;        /* 深灰色/黑色 */
  letter-spacing: 1px;   /* 字间距 */
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

/* === 右侧整体 === */
.nav-right {
  display: flex;
  align-items: center;
  gap: 40px;
}

/* === 菜单链接 === */
.nav-links {
  display: flex;
  gap: 30px;
}

.nav-links a {
  text-decoration: none;
  color: #555;
  font-size: 16px;
  transition: color 0.2s;
}

.nav-links a:hover {
  color: #879bad;
}

.nav-links a.active {
  color: #879bad; /* 激活态颜色也用这个蓝灰 */
  font-weight: 600;
}

/* === 未登录链接 === */
.login-link {
  text-decoration: none;
  color: #879bad;
  font-weight: 600;
}

/* === 用户名展示 (图一模式) === */
.user-profile {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #666;
  transition: opacity 0.2s;
}

.user-profile:hover {
  opacity: 0.8;
}

.avatar-icon {
  font-size: 24px;
  color: #b0c4de; /* 头像图标稍微淡一点 */
}

.nickname {
  font-size: 15px;
  font-weight: 500;
  color: #333;
}

/* === 退出登录按钮 (图二模式 - 红色) === */
.logout-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: transparent;
  border: 1px solid #ff6b6b;
  color: #ff6b6b;
  padding: 5px 14px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.logout-btn:hover {
  background-color: #ff6b6b;
  color: #fff;
}
</style>