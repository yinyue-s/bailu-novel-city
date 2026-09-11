<!-- 
  Copyright (c) 2025 
  All rights reserved.
-->
<!-- 
  Copyright (c) 2025 
  All rights reserved.
-->
<template>
  <div id="app">
    <!-- 导航栏：通过 meta 控制显示/隐藏 -->
     <NavBar v-if="!$route.meta.hideNavbar" />
    
    <!-- 路由出口：各页面的内容会渲染在这里
         没有额外的包裹层，没有 flex 影响，完全交由页面自己控制布局 
    -->
    <router-view />
    
    <!-- 页脚：通过 meta 控制显示/隐藏 -->
    <Footer v-if="!$route.meta.hideFooter" />
  </div>
</template>

<script setup>
import NavBar from '@/components/NavBar.vue';
import Footer from '@/components/Footer.vue';
</script>

<!-- 删除所有 style，确保不影响各页面独立的 CSS -->