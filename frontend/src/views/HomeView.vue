/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
/* ==========================================================================
   说明: 首页页面
   - 对应原 home.html
   ========================================================================== */
<template>
  <div class="home-container">
    <!-- 搜索部分 (Hero Section) -->
    <div class="hero-section">
      <div class="logo-watermark">
        <img src="@/assets/images/白鹭文学城log.png" alt="Background Logo" />
      </div>
      
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchQuery" 
          @keyup.enter="handleSearch"
          placeholder="搜索小说、作者..." 
        />
        <div class="search-btn-box">
          <button class="search-btn" @click="handleSearch" :disabled="isLoading">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
          </button>
        </div>
      </div>
    </div>

    <!-- 分类卡片区域 -->
    <div class="section-container">
      <div class="section-header">
        <h3>小说分类</h3>
        <span v-if="isUsingMock" style="font-size:12px; color:#aaa; margin-left:10px;">(离线模式)</span>
        <a href="#" class="view-all">查看全部 →</a>
      </div>
      
      <div class="category-grid">
        <div class="category-card" v-for="(item, index) in categories" :key="item.id || index">
          <div class="cat-icon">
            <img v-if="item.isImage" :src="item.icon" alt="icon" />
            <span v-else>{{ item.icon }}</span>
          </div> 
          <div class="cat-text">
            <h4>{{ item.name }}</h4>
            <p>{{ item.desc }}</p>
            <span class="count">{{ item.count ? item.count + '万本' : '海量书籍' }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部双列布局：榜单 + 推荐 -->
    <div class="section-container main-content">
      <!-- 热门榜单  -->
      <div class="hot-list-section">
        <div class="section-header">
          <h3>热门榜单</h3>
          <div class="tabs">
            <span class="tab-item active">周榜</span>
            <span class="tab-item">月榜</span>
            <span class="tab-item">总榜</span>
          </div>
        </div>
        <div class="ranking-list">
          <div class="rank-item" v-for="(book, index) in hotBooks" :key="index">
            <div class="rank-left">
              <div class="rank-num" :class="'rank-' + (index + 1)">{{ index + 1 }}</div>
              <span class="book-name">{{ book.title }}</span>
              <span class="status-tag" :class="book.status === '连载中' ? 'ongoing' : 'finished'">{{ book.status }}</span>
            </div>
            <!-- 榜单是假数据，这里直接用 book.id 传给 addToShelf 即可 -->
            <button class="add-shelf-btn" @click="addToShelf(book.id)">+ 加入书架</button>
          </div>
        </div>
      </div>

      <!-- 新品推荐 -->
      <div class="new-recommend-section">
        <div class="section-header">
          <h3>新品推荐</h3>
          <a href="#" class="view-all">更多新品</a>
        </div>
        <div class="recommend-list">
          <div class="rec-item" v-for="(book, index) in newBooks" :key="index">
            <div class="rec-info">
              <span class="book-name">{{ book.title }}</span>
              <span class="status-tag ongoing">连载中</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索结果弹窗 -->
    <div class="search-modal-overlay" v-if="showModal" @click.self="closeModal">
      <div class="search-modal-content">
        <div class="modal-header">
          <h3>搜索结果: "{{ searchQuery }}"</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <div v-if="isLoading" class="loading-state">搜索中...</div>
          <div v-else-if="!searchResults || searchResults.length === 0" class="empty-state">暂无相关小说</div>
          <div v-else class="result-list">
            <!-- 注意：这里的字段全部来自 novel 表：novelID / novelName / introduction / status / wordCount / coverUrl -->
            <div class="result-item" v-for="book in searchResults" :key="book.novelID">
              <div class="book-cover-placeholder">
                <!-- 如果以后 coverUrl 有值，可以改成 <img :src="book.coverUrl" /> -->
                📖
              </div> 
              <div class="book-info">
                <h4>{{ book.novelName }}</h4>
                <!-- authorID 暂时是数字外键，这里先不显示作者名 -->
                <p class="author">作者ID: {{ book.authorID || '未知' }}</p>
                <p class="desc">{{ book.introduction || '暂无简介' }}</p>
                <p class="meta">
                  <span>状态：{{ book.status }}</span>
                  <span style="margin-left: 12px;">字数：{{ book.wordCount }}</span>
                </p>
              </div>
              <!-- 这里传的是数据库里的主键 novelID -->
              <button class="modal-add-btn" @click="addToShelf(book.novelID)">加入书架</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// --- 配置区域 ---
const API = {
  CATEGORY_LIST: '/api/categories', 
  SEARCH: '/api/novels/search',     
  ADD_SHELF: '/api/bookshelf/add'
};

// --- 模拟数据 (本地兜底) ---
const MOCK_CATEGORIES = [
  { id: 1, name: '玄幻', desc: '热血玄幻世界', count: '2.5', icon: '🐉', isImage: false },
  { id: 2, name: '言情', desc: '浪漫都市爱情', count: '2.3', icon: '✒️', isImage: false },
  { id: 3, name: '科幻', desc: '探索未来星际', count: '1.8', icon: '🤖', isImage: false },
  { id: 4, name: '悬疑', desc: '解开重重谜团', count: '2.5', icon: '💙', isImage: false },
  { id: 5, name: '历史', desc: '重回历史长河', count: '2.5', icon: '📜', isImage: false },
];

const hotBooks = ref([
  { id: 101, title: '一剑星河渡', status: '连载中' },
  { id: 102, title: '都市神医', status: '已完结' },
  { id: 103, title: '仙逆凡尘', status: '连载中' },
  { id: 104, title: '大宋江山', status: '连载中' },
]);

const newBooks = ref([
  { id: 201, title: '混沌剑神' },
  { id: 202, title: '民国奇探' },
  { id: 203, title: '星际恋人' },
]);

// --- 响应式状态 ---
const categories = ref([]);
const isUsingMock = ref(false); // 用于标记当前是否在使用模拟数据
const searchQuery = ref('');
const showModal = ref(false);
const isLoading = ref(false);
const searchResults = ref([]);

// --- 分类加载 ---
const loadCategories = async () => {
  try {
    const token = localStorage.getItem('token');

    const response = await fetch(API.CATEGORY_LIST, {
      method: 'GET',
      headers: {
        'Authorization': token ? `Bearer ${token}` : ''
      }
    });

    if (!response.ok) throw new Error('API Response Not OK');

    const resData = await response.json();
    console.log('分类接口返回数据:', resData);

    let list;

    // 情况 1：后端直接返回数组：[ {id, categoryName, ...}, ... ]
    if (Array.isArray(resData)) {
      list = resData;
    }
    // 情况 2：后端返回 { code, data: [...] }
    else if (resData.code === 200 || resData.code === 0) {
      list = resData.data || [];
    }
    else if (resData.success && Array.isArray(resData.data)) {
      list = resData.data;
    } else {
      throw new Error(resData.msg || resData.message || '分类接口返回格式不符合预期');
    }

    categories.value = list.map(item => ({
      id: item.id,
      name: item.categoryName || item.name,
      desc: item.description || '精彩小说等你发现',
      count: item.bookCount || '2.5',
      // icon: item.iconUrl || '📚',
      isImage: !!item.iconUrl
    }));

    isUsingMock.value = false;
    console.log('✅ 已加载数据库分类数据');

  } catch (error) {
    console.warn('⚠️ 无法连接后端或接口异常，切换至模拟数据模式:', error);
    categories.value = MOCK_CATEGORIES;
    isUsingMock.value = true;
  }
};

// 页面加载时执行
onMounted(() => {
  loadCategories();
});

// --- 搜索与书架逻辑 ---
const closeModal = () => (showModal.value = false);

const handleSearch = async () => {
  if (!searchQuery.value.trim()) return;

  showModal.value = true;
  isLoading.value = true;
  searchResults.value = [];

  try {
    const token = localStorage.getItem('token');
    const url = `${API.SEARCH}?keyword=${encodeURIComponent(searchQuery.value)}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Authorization': token ? `Bearer ${token}` : ''
      }
    });

    if (response.status === 401) {
      alert('登录已过期或未登录，请先登录！');
      return;
    }

    const resData = await response.json();
    console.log('搜索接口返回原始数据:', resData);
    console.log('success 字段类型:', typeof resData.success);

    // ==== 这里开始是新的判断逻辑 ====
    // 1. 如果直接返回数组
    if (Array.isArray(resData)) {
      searchResults.value = resData;
    }
    // 2. 只要 data 是数组，就认为是正确结果，不再强依赖 success
    else if (resData && Array.isArray(resData.data)) {
      searchResults.value = resData.data;
    }
    // 3. 兜底：如果里面还有一层 result/list 之类的，可以在这里继续加
    else {
      console.warn('搜索接口返回结构目前不识别，请看上面的 resData 输出');
      alert(resData.message || resData.msg || '搜索失败');
    }
    // ==== 新判断逻辑结束 ====

  } catch (error) {
    console.error('搜索失败', error);
    alert('搜索请求失败');
  } finally {
    isLoading.value = false;
  }
};

const addToShelf = async (novelID) => {
  console.log('开始加入书架，bookId:', novelID);
  if (!novelID) {
    return alert('无法加入书架：书籍ID不存在');
  }

  const username = localStorage.getItem('username');
  let token = localStorage.getItem('token');

  if (!username) {
    return alert('请先登录');
  }

  if (!token) {
    return alert('未登录或Token无效');
  }

  // 处理 token：去掉前缀，避免 BearerBearer 之类的问题
  token = token.trim();
  if (token.startsWith('Bearer ')) {
    token = token.substring(7);
  }

  // 简单解析 token 验证是否过期
  function parseJwt(t) {
    try {
      const base64Url = t.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (e) {
      console.error('解析token失败:', e);
      return null;
    }
  }

  const payload = parseJwt(token);
  if (!payload) {
    alert('登录已过期，请重新登录');
    localStorage.clear();
    window.location.href = '/login';
    return;
  }

  try {
    const response = await fetch(API.ADD_SHELF, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        username,
        novelID       
      })
    });

    const resData = await response.json();
    console.log('加入书架响应:', resData);

    if (resData.success || resData.code === 200) {
      alert('加入成功');
    } else {
      alert(resData.message || resData.msg || '加入失败');
    }
  } catch (e) {
    console.error('请求错误详情:', e);
    alert('请求失败: ' + e.message);
  }
};
</script>

<!-- 引入 CSS -->
<style scoped src="@/assets/styles/home.css"></style>
