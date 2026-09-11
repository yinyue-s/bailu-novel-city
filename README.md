# 白鹭文学城 · 小说阅读平台

前后端分离的小说阅读平台，包含用户注册登录、小说检索、个人书架与个人中心四个模块。

## 功能

- **用户模块**：注册、登录、令牌刷新、个人信息查询，基于 Spring Security + JWT 实现
- **小说模块**：书名关键词模糊检索、按分类查询、按作者查询、小说详情
- **个人书架**：加入与移出书架、书架分组的新增/修改/删除、书架卡片置顶、最近阅读进度记录
- **书架详情**：书架表与小说表联表查询，转换为前端展示用的 VO 返回
- **前端**：Vue 3 单页应用，含登录态路由守卫（`meta.requiresAuth`）与接口统一封装

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 后端 | Java 18、Spring Boot 3.2、Spring Security、jjwt 0.11.5、MyBatis 3.0.3、Jackson |
| 数据库 | MySQL 8 |
| 前端 | Vue 3.4、Vue Router 4、Vite 5、axios |

## 目录结构

```
backend/
  pom.xml
  src/main/java/com/bailu/
    controller/     # 接口层：认证、小说、书架、分类、用户
    service/        # 业务层
    mapper/         # MyBatis 数据访问接口（注解 SQL）
    entity/         # 实体与 DTO / VO
    filter/         # JWT 过滤器
    util/           # JWT 签发与校验
  src/main/resources/application.yml
frontend/
  src/
    api/            # axios 请求封装
    views/          # 首页、书架、论坛、登录、个人中心
    router/         # 路由与登录态守卫
    components/     # 导航栏、页脚
    assets/         # 样式与图片
```

## 快速开始

1. 在 MySQL 中创建数据库 `NovelCity`，并导入 `backend/schema.sql`（内含 7 张表的建表语句：用户、作者、小说、分类、稿件、书架、书架分组，含主外键、唯一索引与普通索引）。
2. 配置数据库连接。`application.yml` 中使用环境变量占位符，可直接设置：

```bash
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=NovelCity
set DB_USERNAME=root
set DB_PASSWORD=你的密码
set JWT_SECRET=你的密钥
```

3. 启动后端：

```bash
cd backend
mvn spring-boot:run
```

4. 启动前端：

```bash
cd frontend
npm install
npm run dev
```

## 说明

- 仓库不含 `node_modules`、`target` 等依赖与构建产物。
- `application.yml` 中的数据库密码与 JWT 密钥已替换为环境变量占位符，请勿提交真实凭据；本地开发可另建 `application-local.yml`（已加入 `.gitignore`）。
- 仓库暂未包含建表脚本，需要时可从本地数据库导出 `schema.sql` 一并提交。
