/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
/* ==========================================================================
   说明: 论坛页面
   - 对应原 forum.html
   - 动态渲染话题列表，支持标签样式动态绑定
   ========================================================================== */
<template>
   <div class="forum-page">

      <div class="container forum-container">
         <!-- 搜索与发布区域 -->
         <div class="search-publish">
            <div class="search-bar">
               <input type="text" placeholder="搜索感兴趣的话题...">
               <button class="search-btn"><i class="fas fa-search"></i></button>
            </div>
            <!-- 发布按钮在右侧 -->
            <button class="publish-btn">
               <i class="fas fa-plus"></i> 发布
            </button>
         </div>

         <!-- 标签切换：动态绑定 active 类 -->
         <div class="tab-switch">
            <button 
               class="tab-btn" 
               :class="{ active: currentTab === 'discussion' }"
               @click="switchTab('discussion')"
            >
               话题讨论
            </button>
            <button 
               class="tab-btn" 
               :class="{ active: currentTab === 'booklist' }"
               @click="switchTab('booklist')"
            >
               书单分享
            </button>
         </div>

         <!-- 话题列表：渲染 filteredTopics -->
         <div class="topic-list">
            <div v-for="topic in filteredTopics" :key="topic.id" class="topic-card">
               
               <!-- 
                  布局关键修改：
                  将标题和标签放在同一个容器(h3)内，
                  利用之前写的 h3 { display: flex; justify-content: space-between; } 样式
               -->
               <h3>
                  <span class="topic-title">{{ topic.title }}</span>
                  <span class="tag">{{ topic.tagName }}</span>
               </h3>
               
               <p class="topic-desc">{{ topic.desc }}</p>
               
               <div class="topic-meta">
                  <span><i class="fas fa-user"></i> {{ topic.author }}</span>
                  <span><i class="fas fa-clock"></i> {{ topic.time }}</span>
                  <!-- 根据类型显示不同的 meta 信息 -->
                  <span v-if="topic.type === 'booklist'"><i class="fas fa-book"></i> 包含 {{ topic.bookCount }} 本书</span>
                  <span v-else><i class="fas fa-comment-dots"></i> {{ topic.replyCount }} 回复</span>
                  
                  <span><i class="fas fa-star"></i> {{ topic.collectCount }} 收藏</span>
               </div>
            </div>
         </div>

         <!-- 分页 -->
         <div class="pagination">
            <button class="page-btn prev"><i class="fas fa-chevron-left"></i></button>
            <button class="page-btn active">1</button>
            <button class="page-btn">2</button>
            <button class="page-btn">3</button>
            <button class="page-btn next"><i class="fas fa-chevron-right"></i></button>
         </div>
      </div>
   </div>
</template>

<script setup>
import { ref, computed } from 'vue';
// 引入你之前的 CSS 文件
import '@/assets/styles/forum.css';

// 1. 定义当前激活的 Tab，默认是 'booklist' (书单分享)
const currentTab = ref('booklist');

// 2. 切换 Tab 的方法
const switchTab = (tabName) => {
   currentTab.value = tabName;
};

// 模拟数据：包含两种类型 (type: 'booklist' | 'discussion')
const allTopics = [
   { 
     id: 1, 
     type: 'booklist',
     tagName: '书单推荐',
     title: '【完结书单】那些让我熬夜看完的精品小说', 
     desc: '整理了一批我熬夜看完的完结精品小说，涵盖玄幻、言情、科幻、悬疑等多个题材，每本都是经过时间考验的佳作，书荒的朋友可以收藏...',
     author: '小说达人', 
     time: '3天前',
     bookCount: 12,
     collectCount: 238
   },
   { 
     id: 2, 
     type: 'booklist',
     tagName: '书单推荐',
     title: '【无CP书单】大女主的成长之路', 
     desc: '专门整理的无CP大女主书单，女主独立自强，搞事业为主，没有恋爱脑，情节精彩，逻辑在线，适合喜欢看女主搞事业的读者...',
     author: '独立女性', 
     time: '1周前',
     bookCount: 8,
     collectCount: 176
   },
   // 增加一些话题讨论的数据，用于演示切换效果
   { 
     id: 3, 
     type: 'discussion',
     tagName: '热门讨论',
     title: '大家觉得现在的修仙文是不是套路化太严重了？', 
     desc: '最近看了几本新书，感觉全是废柴退婚流或者系统签到流，有没有那种设定比较新颖，不落俗套的修仙文推荐？来聊聊你的看法。',
     author: '老书虫', 
     time: '2小时前',
     replyCount: 56, // 话题通常看回复数
     collectCount: 12
   },
   { 
     id: 4, 
     type: 'discussion',
     tagName: '求书求助',
     title: '找一本很多年前看过的科幻小说，主角有一把激光剑', 
     desc: '依稀记得主角叫什么风，背景是星际战争，开局是在一个垃圾星捡破烂。有没有大佬知道书名的？在线等，挺急的！',
     author: '星际迷航', 
     time: '5小时前',
     replyCount: 8,
     collectCount: 3
   },
];

// 3. 计算属性：根据 currentTab 过滤显示的数据
const filteredTopics = computed(() => {
   return allTopics.filter(topic => topic.type === currentTab.value);
});

</script>