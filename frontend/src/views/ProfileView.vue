/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
/* ==========================================================================
   说明: 个人中心页面
   - 对应原 profile.html
   - 使用 mock 数据模拟用户信息
   ========================================================================== */
<template>
  <div class="profile-page">
    
   
    <!-- 2. 主体内容背景 -->
    <div class="main-bg">
       <div class="container">
          
          <!-- 白色大卡片容器 -->
          <div class="profile-card-container">
             
             <!-- 上半部分：个人信息 -->
             <div class="user-info-section">
                <!-- 头像 -->
                <div class="avatar-wrapper">
                   <img src="@/assets/images/默认头像.jpeg" alt="头像">
                   <div class="camera-icon"><i class="fas fa-camera"></i></div>
                </div>

                <!-- 信息细节 -->
                <div class="info-details">
                   <div class="name-row">
                      <h2 class="nickname">{{ userInfo.nickname }}</h2>
                      <span class="level-badge">LV.{{ userInfo.level }} 进阶读者</span>
                   </div>

                   <!-- 5项数据统计 -->
                   <div class="stats-row">
                      <div class="stat-item">
                         <div class="stat-num">{{ userInfo.stats.collection }}</div>
                         <div class="stat-label">书架收藏</div>
                      </div>
                      <div class="stat-item">
                         <div class="stat-num">{{ userInfo.stats.time }}</div>
                         <div class="stat-label">阅读时长(小时)</div>
                      </div>
                      <div class="stat-item">
                         <div class="stat-num">{{ userInfo.stats.comments }}</div>
                         <div class="stat-label">发表评论</div>
                      </div>
                      <div class="stat-item">
                         <div class="stat-num">{{ userInfo.stats.topics }}</div>
                         <div class="stat-label">发布话题</div>
                      </div>
                      <div class="stat-item">
                         <div class="stat-num">{{ userInfo.stats.likes }}</div>
                         <div class="stat-label">获得点赞</div>
                      </div>
                   </div>
                </div>

                <!-- 编辑资料按钮 (右上角) -->
                <button class="edit-profile-btn">
                   <i class="far fa-edit"></i> 编辑资料
                </button>
             </div>

             <!-- 中间：Tab 导航 -->
             <div class="profile-nav-tabs">
                <div 
                   v-for="tab in tabs" 
                   :key="tab.key"
                   class="nav-item"
                   :class="{ active: currentTab === tab.key }"
                   @click="currentTab = tab.key"
                >
                   {{ tab.name }}
                </div>
             </div>

             <!-- 下半部分：内容展示区 -->
             <div class="content-area">
                
                <!-- 只有在 阅读记录 Tab 下才显示布局 -->
                <div v-if="currentTab === 'history'" class="history-section">
                   <h3 class="section-title">最近阅读</h3>
                   
                   <div class="book-list">
                      <div v-for="book in historyList" :key="book.id" class="book-row-card">
                         <!-- 如果有封面图可以放这里，截图看似没有封面或封面在左侧留白 -->
                         <!-- <div class="book-cover"></div> -->
                         
                         <div class="book-info-col">
                            <h4 class="book-name">{{ book.title }}</h4>
                            <div class="book-meta-row">作者：{{ book.author }}</div>
                            <div class="book-meta-row highlight-blue">阅读至：{{ book.progress }} ({{ book.percent }})</div>
                            <div class="book-meta-row time">上次阅读：{{ book.lastRead }}</div>
                         </div>

                         <button class="continue-read-btn">继续阅读</button>
                      </div>
                   </div>
                </div>

                <!-- 其他 Tab 占位 -->
                <div v-else class="empty-state">
                   暂无内容
                </div>

             </div>

          </div>

       </div>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import '@/assets/styles/profile.css';

const currentTab = ref('history');

const tabs = [
  { key: 'history', name: '阅读记录' },
  { key: 'posts', name: '我的发帖' },
  { key: 'comments', name: '我的评论' },
  { key: 'settings', name: '账号设置' }
];

const userInfo = ref({
  nickname: '白鹭',
  level: 5,
  avatar: '', 
  stats: {
      collection: 12,
      time: 156,
      comments: 23,
      topics: 8,
      likes: 328
  }
});

const historyList = ref([
  { 
      id: 1, 
      title: '一剑星河渡', 
      author: '剑仙', 
      progress: '第89章 星河秘境', 
      percent: '65%',
      lastRead: '2小时前' 
  },
  { 
      id: 2, 
      title: '星际殖民', 
      author: '星空旅者', 
      progress: '第56章 火星基地', 
      percent: '42%',
      lastRead: '昨天 19:30' 
  }
]);

const handleLogout = () => {
    alert("退出登录");
};
</script>