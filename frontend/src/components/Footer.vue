/*
 *   Copyright (c) 2025 
 *   All rights reserved.
 */
<template>
  <footer class="site-footer">
    <div class="footer-container">
      
      <!-- 上部分：Logo、链接、社交图标 -->
      <div class="footer-top">
        
        <!-- 左侧：Logo区域 - 放大Logo -->
        <div class="footer-logo-section">
          <img src="@/assets/images/白鹭文学城log.png" alt="白鹭文学城" class="footer-logo" @click="$router.push('/')">
          <p class="logo-slogan">发现属于你的精彩故事</p>
        </div>

        <!-- 中间：链接列表 (三列) - 居中显示 -->
        <div class="footer-links-section">
          <div class="link-column">
            <h4>关于我们</h4>
            <ul class="link-list">
              <li><a href="#">平台简介</a></li>
              <li><a href="#">联系我们</a></li>
              <li><a href="#">加入我们</a></li>
            </ul>
          </div>
          <div class="link-column">
            <h4>帮助中心</h4>
            <ul class="link-list">
              <li><a href="#">用户指南</a></li>
              <li><a href="#">常见问题</a></li>
              <li><a href="#">意见反馈</a></li>
            </ul>
          </div>
          <div class="link-column">
            <h4>法律条款</h4>
            <ul class="link-list">
              <li><a href="#">用户协议</a></li>
              <li><a href="#">隐私政策</a></li>
              <li><a href="#">版权声明</a></li>
            </ul>
          </div>
        </div>

        <!-- 右侧：关注我们 -->
        <div class="footer-social-section">
          <h4>关注我们</h4>
          <p class="social-desc">关注我们获取最新资讯</p>
          <div class="social-icons">
            <a href="#" class="social-icon" title="微信">
              <i class="fab fa-weixin"></i>
            </a>
            <a href="#" class="social-icon" title="微博">
              <i class="fab fa-weibo"></i>
            </a>
            <a href="#" class="social-icon" title="QQ">
              <i class="fab fa-qq"></i>
            </a>
          </div>
        </div>
      </div>

      <!-- 分割线 -->
      <div class="footer-divider"></div>

      <!-- 下部分：版权信息 -->
      <div class="footer-bottom">
        <p>&copy; 2025 白鹭文学城 版权所有 | 备案号：XXX-12345678</p>
        <p class="bottom-links">
          <a href="#">友情链接</a> | 
          <a href="#">合作伙伴</a> | 
          <a href="#">网站地图</a>
        </p>
      </div>
    </div>
  </footer>
</template>

<script setup>
// 不需要额外逻辑
</script>

<style scoped>
/* 页脚整体容器 */
.site-footer {
  background-color: var(--white);
  padding: 50px 0 30px;
  border-top: 1px solid var(--border-color);
  margin-top: 60px;
  width: 100%;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* --- 上半部分布局 --- */
.footer-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 40px;
  flex-wrap: wrap;
  gap: 40px;
}

/* Logo 区域 - 进一步放大Logo */
.footer-logo-section {
  flex: 1;
  min-width: 280px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.footer-logo {
  height: 120px; /* 进一步放大Logo */
  width: auto;
  max-width: 240px;
  object-fit: contain;
  cursor: pointer;
  transition: transform 0.3s ease;
  filter: brightness(0.95);
  border-radius: 4px;
}

.footer-logo:hover {
  transform: scale(1.05);
  filter: brightness(1);
}

.logo-slogan {
  margin-top: 12px;
  color: var(--light-text);
  font-size: 15px;
  font-style: italic;
  font-weight: 300;
}

/* 链接列表区域 - 居中显示 */
.footer-links-section {
  flex: 2;
  min-width: 500px;
  display: flex;
  justify-content: center; 
  gap: 60px;
}

.link-column {
  display: flex;
  flex-direction: column;
  align-items: center; /* 居中 */
  text-align: center; /* 文本居中 */
}

.link-column h4 {
  font-size: 18px;
  color: var(--text-color);
  margin-bottom: 20px;
  font-weight: 600;
  /* 取消下划线 */
}

.link-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: center; /* 居中 */
}

.link-list li {
  margin-bottom: 12px;
}

.link-list a {
  text-decoration: none;
  color: var(--light-text);
  font-size: 14px;
  transition: all 0.3s ease;
  display: inline-block;
  padding: 3px 0;
}

.link-list a:hover {
  color: var(--accent-color);
  transform: translateY(-2px);
}

/* 社交图标区域 */
.footer-social-section {
  flex: 1;
  min-width: 200px;
  display: flex;
  flex-direction: column;
  align-items: flex-end; /* 保持右侧对齐 */
}

.footer-social-section h4 {
  font-size: 18px;
  color: var(--text-color);
  margin-bottom: 15px;
  font-weight: 600;
  text-align: right; /* 右侧对齐 */
}

.social-desc {
  color: var(--light-text);
  font-size: 13px;
  margin-bottom: 20px;
  line-height: 1.5;
  text-align: right; /* 右侧对齐 */
}

.social-icons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
  justify-content: flex-end; /* 右侧对齐 */
}

.social-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, var(--secondary-color), #e0e0e0);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--accent-color);
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.social-icon::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
  transition: left 0.5s ease;
}

.social-icon:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
  color: var(--white);
  background: linear-gradient(135deg, var(--accent-color), #7a8b9c);
}

.social-icon:hover::before {
  left: 100%;
}

.social-icon:hover i {
  transform: scale(1.1);
}

/* 分割线 */
.footer-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--border-color), transparent);
  margin: 30px 0;
}

/* 底部版权 */
.footer-bottom {
  text-align: center;
  padding-top: 20px;
}

.footer-bottom p {
  font-size: 14px;
  color: var(--light-text);
  margin: 8px 0;
  line-height: 1.6;
}

.bottom-links {
  margin-top: 15px !important;
}

.bottom-links a {
  color: var(--light-text);
  text-decoration: none;
  margin: 0 10px;
  font-size: 13px;
  transition: color 0.3s ease;
}

.bottom-links a:hover {
  color: var(--accent-color);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .footer-top {
    gap: 30px;
  }
  
  .footer-links-section {
    min-width: 450px;
    gap: 40px;
  }
  
  .footer-logo {
    height: 100px;
    max-width: 200px;
  }
}

@media (max-width: 768px) {
  .site-footer {
    padding: 40px 0 25px;
  }
  
  .footer-top {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 40px;
  }
  
  .footer-logo-section {
    align-items: center;
  }
  
  .footer-logo {
    height: 90px;
    max-width: 180px;
  }
  
  .footer-links-section {
    min-width: 100%;
    justify-content: center;
    gap: 40px;
    flex-wrap: wrap;
  }
  
  .link-column {
    min-width: 150px;
  }
  
  .footer-social-section {
    align-items: center;
  }
  
  .footer-social-section h4,
  .social-desc {
    text-align: center; /* 移动端居中 */
  }
  
  .social-icons {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .footer-logo {
    height: 80px;
    max-width: 160px;
  }
  
  .footer-links-section {
    flex-direction: column;
    align-items: center;
    gap: 30px;
  }
  
  .link-column {
    width: 100%;
    text-align: center;
  }
  
  .link-list li {
    display: inline-block;
    margin: 0 10px 10px;
  }
  
  .social-icon {
    width: 44px;
    height: 44px;
    font-size: 18px;
  }
}
</style>