/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
<template>
  <div class="bookshelf-page">
    <div class="container bookshelf-container">
      <!-- 书架标题 + 筛选排序 -->
      <div class="bookshelf-header">
        <h2>我的书架</h2>
        <div class="header-actions">
          <!-- 筛选：状态 -->
          <div class="filter-wrapper">
            <label>状态：</label>
            <select v-model="filterStatus">
              <option value="">全部</option>
              <option value="连载中">连载中</option>
              <option value="已完结">已完结</option>
            </select>
          </div>

          <!-- 排序：切换 -->
          <button class="action-btn" @click="toggleSort">
            排序：{{ sortLabel }}
          </button>
        </div>
      </div>

      <!-- 分组标签 -->
      <div class="group-tabs">
        <button
          v-for="group in groups"
          :key="group.groupID"
          class="group-tab"
          :class="{ active: currentGroupID === group.groupID }"
          @click="switchGroup(group.groupID)"
        >
          {{ group.groupName }}
          <!-- 默认分组不显示删除 -->
          <span
            v-if="group.groupName !== '默认'"
            class="group-delete"
            @click.stop="deleteGroup(group.groupID)"
          >
            ×
          </span>
        </button>

        <button class="new-group-btn" @click="createGroup">+ 新建分组</button>
      </div>

      <!-- 书籍列表 -->
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="errorMsg" class="error">{{ errorMsg }}</div>
      <div v-else-if="filteredBooks.length === 0" class="books-empty">
        该分组下暂无书籍
      </div>

      <div v-else class="books-list">
        <div class="book-item" v-for="book in filteredBooks" :key="book.shelfID">
          <div class="book-cover">
            <div class="cover-placeholder">📖</div>
          </div>

          <div class="book-info">
            <div class="book-title-row">
              <!-- 书名 -->
              <h3 class="book-title">{{ book.novelName || '未知书名' }}</h3>
              <!-- 状态 -->
              <span
                class="book-status"
                :class="book.status === '连载中' ? 'ongoing' : 'finished'"
              >
                {{ book.status || '未知状态' }}
              </span>
            </div>

            <!-- 简介 -->
            <p class="book-intro">
              {{ book.introduction || '暂无简介' }}
            </p>

            <!-- 字数 + 加入时间 -->
            <div class="book-meta">
              <span>字数：{{ book.wordCount || '未知' }}</span>
              <span class="add-time">加入时间：{{ formatTime(book.addTime) }}</span>
            </div>
          </div>

          <div class="book-actions">
            <!-- 移动分组选择框 -->
            <select
              class="move-select"
              :value="book.groupID"
              @change="onMoveSelectChange(book, $event.target.value)"
            >
              <option
                v-for="g in groups"
                :key="g.groupID"
                :value="g.groupID"
              >
                {{ g.groupName }}
              </option>
            </select>

            <!-- 从书架删除 -->
            <button class="btn-remove" @click="removeFromShelf(book.shelfID)">
              从书架删除
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import '@/assets/styles/bookshelf.css';

const API = {
  GROUP_LIST: '/api/bookshelf/groups',
  GROUP_CREATE: '/api/bookshelf/groups',
  GROUP_DELETE: '/api/bookshelf/groups',
  SHELF_REMOVE: '/api/bookshelf',
  SHELF_MOVE: '/api/bookshelf/move',
  SHELF_DETAIL_LIST: '/api/bookshelf/detailList'  // 修正拼写错误
};

const groups = ref([]);
const currentGroupID = ref(null);
const allBooks = ref([]);  // 所有书籍数据
const loading = ref(false);
const errorMsg = ref('');

// 筛选 & 排序
const filterStatus = ref('');
const sortType = ref('time');

// 排序按钮显示文本
const sortLabel = computed(() => {
  return sortType.value === 'time' ? '按加入时间' : '按书名';
});

// 根据当前分组、筛选 + 排序得到最终展示列表
const filteredBooks = computed(() => {
  // 先按当前分组筛选
  let list = [...allBooks.value];
  
  if (currentGroupID.value) {
    list = list.filter(b => String(b.groupID) === String(currentGroupID.value));
  }
  
  // 状态筛选
  if (filterStatus.value) {
    list = list.filter(b => b.status === filterStatus.value);
  }

  // 排序
  if (sortType.value === 'time') {
    list.sort((a, b) => new Date(b.addTime) - new Date(a.addTime));
  } else {
    list.sort((a, b) => (a.novelName || '').localeCompare(b.novelName || ''));
  }

  return list;
});

// 工具：取 token
const getToken = () => {
  let token = localStorage.getItem('token');
  if (!token) return null;
  token = token.trim();
  if (token.startsWith('Bearer ')) token = token.substring(7);
  return token;
};

//
// 分组相关
//

// 加载分组
const loadGroups = async () => {
  const token = getToken();
  if (!token) {
    errorMsg.value = '请先登录';
    return;
  }
  
  try {
    const res = await fetch(API.GROUP_LIST, {
      headers: { Authorization: `Bearer ${token}` }
    });
    
    if (res.status === 401) {
      errorMsg.value = '登录已过期，请重新登录';
      return;
    }
    
    const data = await res.json();
    console.log('分组列表:', data);

    if (data.success && Array.isArray(data.data)) {
      groups.value = data.data;
    } else if (Array.isArray(data)) {
      groups.value = data;
    } else {
      errorMsg.value = data.message || '加载分组失败';
      return;
    }

    // 默认选中第一个分组
    if (groups.value.length > 0 && currentGroupID.value == null) {
      currentGroupID.value = groups.value[0].groupID;
    }
  } catch (e) {
    console.error('加载分组失败:', e);
    errorMsg.value = '加载分组失败';
  }
};

// 创建分组
const createGroup = async () => {
  const name = window.prompt('请输入分组名称：', '');
  if (!name || !name.trim()) return;

  const token = getToken();
  if (!token) {
    alert('请先登录');
    return;
  }

  try {
    const res = await fetch(API.GROUP_CREATE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({ groupName: name.trim() })
    });
    
    const data = await res.json();
    console.log('创建分组返回:', data);
    
    if (data.success && data.data) {
      groups.value.push(data.data);
      // 切换到新创建的分组
      currentGroupID.value = data.data.groupID;
    } else {
      alert(data.message || '创建分组失败');
    }
  } catch (e) {
    console.error('创建分组失败:', e);
    alert('创建分组失败');
  }
};

// 删除分组
const deleteGroup = async (groupID) => {
  if (!window.confirm('确定要删除该分组吗？')) {
    return;
  }
  
  const token = getToken();
  if (!token) {
    alert('请先登录');
    return;
  }
  
  try {
    const url = `${API.GROUP_DELETE}/${groupID}`;
    const res = await fetch(url, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    
    const data = await res.json();
    console.log('删除分组返回:', data);
    
    if (data.success) {
      // 删除分组
      groups.value = groups.value.filter(g => g.groupID !== groupID);
      
      // 如果删除了当前选中的分组，切换到第一个分组
      if (currentGroupID.value === groupID) {
        currentGroupID.value = groups.value.length > 0 ? groups.value[0].groupID : null;
      }
      
      // 删除分组后，需要重新加载书籍数据
      await loadBookshelf();
    } else {
      alert(data.message || '删除分组失败');
    }
  } catch (e) {
    console.error('删除分组失败:', e);
    alert('删除分组失败');
  }
};

//
// 书架数据相关
//

// 加载书架数据
const loadBookshelf = async () => {
  loading.value = true;
  errorMsg.value = '';
  
  try {
    const token = getToken();
    if (!token) {
      errorMsg.value = '请先登录';
      return;
    }

    // 使用详细列表接口
    const res = await fetch(API.SHELF_DETAIL_LIST, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (res.status === 401) {
      errorMsg.value = '登录已过期，请重新登录';
      return;
    }

    if (!res.ok) {
      throw new Error('网络错误：' + res.status);
    }

    const body = await res.json();
    console.log('书架数据:', body);

    if (!body.success) {
      throw new Error(body.message || '获取书架失败');
    }

    // 确保数据是数组
    if (Array.isArray(body.data)) {
      allBooks.value = body.data;
    } else if (body.data && Array.isArray(body.data.list)) {
      // 如果数据结构是 { success: true, data: { list: [...] } }
      allBooks.value = body.data.list;
    } else {
      allBooks.value = [];
    }
    
    console.log('处理后的书籍数据:', allBooks.value);
  } catch (e) {
    console.error('加载书架失败:', e);
    errorMsg.value = e.message || '加载失败';
  } finally {
    loading.value = false;
  }
};

// 切换分组
const switchGroup = (groupID) => {
  currentGroupID.value = groupID;
};

//
// 移动分组 & 删除
//

// 调用后端接口移动分组
const moveBookToGroup = async (shelfID, newGroupId) => {
  const token = getToken();
  if (!token) {
    alert('请先登录');
    return false;
  }

  try {
    const res = await fetch(API.SHELF_MOVE, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({
        shelfID: shelfID,
        groupID: Number(newGroupId)
      })
    });

    const data = await res.json();
    console.log('移动分组返回:', data);
    
    if (data.success) {
      // 更新本地数据
      const bookIndex = allBooks.value.findIndex(b => b.shelfID === shelfID);
      if (bookIndex !== -1) {
        allBooks.value[bookIndex].groupID = Number(newGroupId);
      }
      return true;
    } else {
      alert(data.message || '移动失败');
      return false;
    }
  } catch (e) {
    console.error('移动分组请求失败:', e);
    alert('移动分组请求失败');
    return false;
  }
};

// 下拉框变更事件
const onMoveSelectChange = async (book, newGroupId) => {
  if (String(book.groupID) === String(newGroupId)) return;

  const ok = await moveBookToGroup(book.shelfID, newGroupId);
  if (!ok) {
    // 如果移动失败，重新加载数据确保同步
    await loadBookshelf();
  }
};

// 从书架删除小说
const removeFromShelf = async (shelfID) => {
  if (!window.confirm('确定要从书架删除这本小说吗？')) return;

  const token = getToken();
  if (!token) {
    alert('请先登录');
    return;
  }
  
  try {
    const url = `${API.SHELF_REMOVE}/${shelfID}`;
    const res = await fetch(url, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    
    const data = await res.json();
    console.log('删除书架记录返回:', data);
    
    if (data.success) {
      // 从本地数据中删除
      allBooks.value = allBooks.value.filter(b => b.shelfID !== shelfID);
    } else {
      alert(data.message || '删除失败');
    }
  } catch (e) {
    console.error('删除失败:', e);
    alert('删除失败');
  }
};

//
// 其它工具
//

// 排序切换
const toggleSort = () => {
  sortType.value = sortType.value === 'time' ? 'name' : 'time';
};

// 格式化时间
const formatTime = (t) => {
  if (!t) return '未知';
  const d = new Date(t);
  if (isNaN(d.getTime())) return t;
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
};

// 监听分组变化，重新加载书籍
watch(currentGroupID, (newVal) => {
  console.log('分组切换至:', newVal);
});

// 初始化加载
onMounted(async () => {
  console.log('初始化书架页面');
  // 先加载分组
  await loadGroups();
  // 再加载书架数据
  await loadBookshelf();
});
</script>

