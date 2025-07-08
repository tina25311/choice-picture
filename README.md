# 图片喜好调查系统

一个基于Spring Boot + Vue.js的图片喜好调查系统，支持多语言、Redis缓存和响应式设计。

## 功能特性

- 🎯 **前后端分离架构** - Spring Boot + Vue.js
- 🌍 **多语言支持** - 自动检测浏览器语言（中文/英文）
- 📊 **实时进度显示** - 调查进度可视化
- 🎨 **精美动画效果** - 图片切换和完成动画
- 💾 **数据持久化** - MySQL数据库存储
- ⚡ **Redis缓存** - 提升系统性能
- 📱 **响应式设计** - 支持移动端和桌面端
- 🔍 **用户信息记录** - IP、User-Agent、浏览器语言

## 技术栈

### 后端
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Data Redis
- MySQL 8.0
- Redis
- Lombok

### 前端
- Vue 3
- Vue Router
- Vue I18n
- Axios
- Vite

## 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+

### 1. 数据库配置
```sql
-- 创建数据库
CREATE DATABASE choice_picture CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置文件
修改 `backend/src/main/resources/application.yml` 中的数据库和Redis配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/choice_picture
    username: your_username
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
```

### 3. 启动服务

#### 后端服务
```bash
cd backend
mvn spring-boot:run
```

#### 前端服务
```bash
cd frontend
npm install
npm run dev
```

### 4. 添加图片
将8张调查图片（1080x1920像素，JPEG格式）放入 `frontend/public/images/` 目录，命名为：
- image1.jpg
- image2.jpg
- image3.jpg
- image4.jpg
- image5.jpg
- image6.jpg
- image7.jpg
- image8.jpg

## 项目结构

```
choice-picture/
├── backend/                 # 后端项目
│   ├── src/main/java/
│   │   └── com/example/choicepicture/
│   │       ├── config/      # 配置类
│   │       ├── controller/  # 控制器
│   │       ├── dto/         # 数据传输对象
│   │       ├── entity/      # 实体类
│   │       ├── repository/  # 数据访问层
│   │       └── service/     # 业务逻辑层
│   └── src/main/resources/
│       ├── application.yml  # 配置文件
│       └── schema.sql       # 数据库脚本
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── components/      # Vue组件
│   │   ├── views/           # 页面组件
│   │   ├── services/        # API服务
│   │   └── i18n/            # 国际化配置
│   └── public/images/       # 图片资源
└── README.md
```

## API接口

### 获取图片列表
```
GET /api/survey/images
```

### 提交调查
```
POST /api/survey/submit
Content-Type: application/json

{
  "imageRatings": [
    {
      "imageId": 1,
      "isLiked": true
    }
  ]
}
```

### 获取统计信息
```
GET /api/survey/stats
```

## 数据库设计

### survey_responses 表
- id: 主键
- ip_address: IP地址
- user_agent: 用户代理
- browser_language: 浏览器语言
- created_at: 创建时间

### image_ratings 表
- id: 主键
- survey_response_id: 调查响应ID
- image_id: 图片ID
- is_liked: 是否喜欢

## 部署说明

### 生产环境配置
1. 修改数据库连接配置
2. 配置Redis连接
3. 设置跨域允许的域名
4. 配置日志级别

### Docker部署
```bash
# 构建后端镜像
cd backend
docker build -t choice-picture-backend .

# 构建前端镜像
cd frontend
docker build -t choice-picture-frontend .

# 启动服务
docker-compose up -d
```

## 开发说明

### 添加新语言
1. 在 `frontend/src/i18n/index.js` 中添加新的语言配置
2. 更新语言检测逻辑

### 自定义样式
- 全局样式：`frontend/src/assets/main.css`
- 组件样式：各组件内的 `<style>` 标签

### 扩展功能
- 添加新的调查类型
- 实现数据导出功能
- 添加用户管理功能
- 集成第三方登录

## 许可证

MIT License 