# 图片喜好调查系统 - API 和组件文档

## 目录
- [项目概述](#项目概述)
- [后端 API 接口](#后端-api-接口)
- [前端组件](#前端组件)
- [工具函数](#工具函数)
- [配置说明](#配置说明)
- [数据模型](#数据模型)
- [使用示例](#使用示例)

---

## 项目概述

这是一个基于 Spring Boot + Vue.js 的图片喜好调查系统，支持多语言、Redis缓存和响应式设计。用户可以对8张图片进行喜好评分，系统会记录用户信息并提供统计分析功能。

### 技术栈
- **后端**: Spring Boot 3.2.0, MyBatis, Redis, MySQL
- **前端**: Vue 3, Vue Router, Vue I18n, Axios, Chart.js
- **工具**: Vite, Lombok, Maven

---

## 后端 API 接口

### 基础配置
**基础URL**: `https://backend.linuxapp.me/api`  
**请求格式**: JSON  
**响应格式**: JSON

### 1. 获取图片ID列表

**接口地址**: `GET /survey/images`

**描述**: 获取所有可用的图片ID列表

**请求参数**: 无

**响应示例**:
```json
[1, 2, 3, 4, 5, 6, 7, 8]
```

**使用示例**:
```javascript
// 前端调用
const response = await surveyAPI.getImageIds()
const imageIds = response.data
```

---

### 2. 提交调查问卷

**接口地址**: `POST /survey/submit`

**描述**: 提交用户的图片喜好调查数据

**请求头**:
```
Content-Type: application/json
User-Agent: [自动获取]
Accept-Language: [自动获取]
X-Forwarded-For: [可选]
X-Real-IP: [可选]
```

**请求体**:
```json
{
  "imageRatings": [
    {
      "imageId": 1,
      "isLiked": true
    },
    {
      "imageId": 2,
      "isLiked": false
    }
    // ... 其他图片评分
  ]
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "调查提交成功",
  "responseId": 123
}
```

**错误响应**:
```json
{
  "success": false,
  "message": "调查提交失败: 错误详情"
}
```

**验证规则**:
- `imageRatings`: 必填，必须包含8个图片评分
- `imageId`: 必填，整数类型
- `isLiked`: 必填，布尔类型

---

### 3. 获取基础统计信息

**接口地址**: `GET /survey/stats`

**描述**: 获取调查的基础统计信息

**请求参数**: 无

**响应示例**:
```json
{
  "totalResponses": 156,
  "responsesByIp": [
    ["192.168.1.1", 3],
    ["192.168.1.2", 2]
  ]
}
```

---

### 4. 获取详细统计信息

**接口地址**: `GET /survey/stats/detailed`

**描述**: 获取详细的统计分析数据，用于图表展示

**请求参数**: 无

**响应示例**:
```json
{
  "totalResponses": 156,
  "likesByImage": [
    [1, 89],
    [2, 76],
    [3, 102]
  ],
  "dislikesByImage": [
    [1, 67],
    [2, 80],
    [3, 54]
  ],
  "deviceTypeStats": [
    ["移动端", 89],
    ["桌面端", 54],
    ["平板", 13]
  ]
}
```

---

### 5. 清除缓存

**接口地址**: `POST /survey/cache/clear`

**描述**: 清除Redis缓存中的统计数据

**请求参数**: 无

**响应示例**:
```json
{
  "success": true,
  "message": "缓存清除成功"
}
```

---

## 前端组件

### 1. SurveyView - 调查主视图

**文件位置**: `frontend/src/views/SurveyView.vue`

**功能描述**: 
- 展示图片并收集用户喜好评分
- 支持触摸滑动和按钮操作
- 实时显示进度条
- 图片预加载优化

**主要方法**:

#### `loadImageIds()`
```javascript
async loadImageIds() {
  // 从API获取图片ID列表
  // 处理加载失败的容错机制
}
```

#### `rateImage(isLiked)`
```javascript
rateImage(isLiked) {
  // 参数: isLiked - boolean，true表示喜欢，false表示不喜欢
  // 记录用户评分并自动切换到下一张图片
}
```

#### `submitSurvey()`
```javascript
async submitSurvey() {
  // 验证所有图片都已评分
  // 提交数据到后端API
  // 跳转到完成页面
}
```

**事件处理**:
- `handleTouchStart/End`: 触摸滑动手势
- `nextImage`: 切换到下一张图片
- `onImageLoad/Error`: 图片加载状态处理

**使用示例**:
```vue
<template>
  <SurveyView @survey-completed="handleCompletion" />
</template>
```

---

### 2. StatsView - 统计视图

**文件位置**: `frontend/src/views/StatsView.vue`

**功能描述**:
- 展示调查统计结果
- 集成多种图表组件
- 实时数据更新

**集成组件**:
- `SimpleBarChart` - 柱状图
- `SimplePieChart` - 饼图
- `ClickableBarChart` - 可交互柱状图

---

### 3. MusicPlayer - 音乐播放器

**文件位置**: `frontend/src/components/MusicPlayer.vue`

**功能描述**:
- 背景音乐播放控制
- 支持拖拽定位
- 最小化/最大化切换
- 播放列表管理

**主要API**:

#### `togglePlay()`
```javascript
const togglePlay = () => {
  // 切换播放/暂停状态
}
```

#### `playSong(index)`
```javascript
const playSong = (index) => {
  // 参数: index - 歌曲在播放列表中的索引
  // 播放指定歌曲
}
```

#### `addSong()`
```javascript
const addSong = () => {
  // 打开添加歌曲对话框
  // 支持网易云音乐链接解析
}
```

**拖拽功能**:
```javascript
const startDrag = (event) => {
  // 开始拖拽，仅在最小化状态下有效
}
```

---

### 4. Chart 组件系列

#### SimpleBarChart - 简单柱状图
**文件位置**: `frontend/src/components/SimpleBarChart.vue`

**Props**:
```javascript
{
  chartData: Array,    // 图表数据
  title: String,       // 图表标题
  height: Number       // 图表高度
}
```

**使用示例**:
```vue
<SimpleBarChart 
  :chart-data="likeData"
  title="图片喜欢统计"
  :height="300"
/>
```

#### SimplePieChart - 饼图
**文件位置**: `frontend/src/components/SimplePieChart.vue`

**Props**:
```javascript
{
  chartData: Array,    // 饼图数据
  title: String,       // 图表标题
  colors: Array        // 自定义颜色
}
```

#### ClickableBarChart - 可交互柱状图
**文件位置**: `frontend/src/components/ClickableBarChart.vue`

**功能**: 
- 点击柱状图显示对应图片预览
- 支持图片预览弹窗

---

### 5. 图标组件

#### LikeIcon / DislikeIcon
**文件位置**: `frontend/src/components/icons/`

**Props**:
```javascript
{
  isActive: Boolean    // 是否激活状态
}
```

**使用示例**:
```vue
<LikeIcon :is-active="rating === true" />
<DislikeIcon :is-active="rating === false" />
```

---

## 工具函数

### 1. API 服务

**文件位置**: `frontend/src/services/api.js`

**主要功能**:
- Axios实例配置
- 请求/响应拦截器
- API接口封装

**使用示例**:
```javascript
import { surveyAPI } from '@/services/api'

// 获取图片列表
const images = await surveyAPI.getImageIds()

// 提交调查
const result = await surveyAPI.submitSurvey(data)

// 获取统计
const stats = await surveyAPI.getStats()
```

---

### 2. 图片预加载工具

**文件位置**: `frontend/src/utils/imagePreloader.js`

#### `preloadAllImages()`
```javascript
export const preloadAllImages = async () => {
  // 预加载所有调查图片，提升用户体验
  // 返回: Promise
}
```

#### `getPreloadedImage(imageId)`
```javascript
export const getPreloadedImage = (imageId) => {
  // 参数: imageId - 图片ID
  // 返回: HTMLImageElement 或 undefined
}
```

#### `isImagePreloaded(imageId)`
```javascript
export const isImagePreloaded = (imageId) => {
  // 参数: imageId - 图片ID
  // 返回: boolean - 图片是否已预加载
}
```

**使用示例**:
```javascript
import { preloadAllImages, getPreloadedImage } from '@/utils/imagePreloader'

// 在应用启动时预加载
await preloadAllImages()

// 获取预加载的图片
const imgElement = getPreloadedImage(1)
```

---

### 3. 国际化配置

**文件位置**: `frontend/src/i18n/index.js`

**支持语言**:
- 中文 (zh-CN)
- 英文 (en-US)

**自动语言检测**:
```javascript
function detectLanguage() {
  const browserLang = navigator.language || navigator.userLanguage
  return browserLang.startsWith('zh') ? 'zh-CN' : 'en-US'
}
```

**使用示例**:
```vue
<template>
  <h1>{{ $t('welcome.title') }}</h1>
  <button>{{ $t('survey.like') }}</button>
</template>
```

---

## 配置说明

### 1. 后端配置

#### Redis 配置
**文件位置**: `backend/src/main/java/com/example/choicepicture/config/RedisConfig.java`

**缓存策略**:
- 统计数据缓存 (surveyStats)
- 缓存过期策略
- 序列化配置

#### 数据库配置
**配置文件**: `backend/src/main/resources/application-local.yml`

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

---

### 2. 前端配置

#### Vite 配置
**文件位置**: `frontend/vite.config.js`

**主要配置**:
- Vue插件
- 构建优化
- 代理设置

#### 路由配置
**文件位置**: `frontend/src/router/index.js`

**路由列表**:
- `/` - 欢迎页面
- `/survey` - 调查页面
- `/completion` - 完成页面
- `/stats` - 统计页面
- `/test-music` - 音乐测试页面

---

## 数据模型

### 1. 后端实体类

#### SurveyResponse - 调查响应
```java
public class SurveyResponse {
    private Long id;                    // 主键ID
    private String ipAddress;           // IP地址
    private String userAgent;           // 用户代理
    private String browserLanguage;     // 浏览器语言
    private LocalDateTime createdAt;    // 创建时间
    private List<ImageRating> imageRatings; // 图片评分列表
}
```

#### ImageRating - 图片评分
```java
public class ImageRating {
    private Long id;                    // 主键ID
    private Long surveyResponseId;      // 调查响应ID
    private Integer imageId;            // 图片ID
    private Boolean isLiked;            // 是否喜欢
    private SurveyResponse surveyResponse; // 关联的调查响应
}
```

#### SurveySubmissionRequest - 提交请求DTO
```java
public class SurveySubmissionRequest {
    @NotNull
    @Size(min = 8, max = 8)
    private List<ImageRatingRequest> imageRatings;
    
    public static class ImageRatingRequest {
        @NotNull
        private Integer imageId;
        @NotNull
        private Boolean isLiked;
    }
}
```

---

### 2. 数据库表结构

#### survey_responses 表
```sql
CREATE TABLE survey_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ip_address VARCHAR(45) NOT NULL,
    user_agent TEXT,
    browser_language VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_ip_address (ip_address),
    INDEX idx_created_at (created_at)
);
```

#### image_ratings 表
```sql
CREATE TABLE image_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_response_id BIGINT NOT NULL,
    image_id INT NOT NULL,
    is_liked BOOLEAN NOT NULL,
    FOREIGN KEY (survey_response_id) REFERENCES survey_responses(id) ON DELETE CASCADE,
    INDEX idx_survey_response_id (survey_response_id),
    INDEX idx_image_id (image_id),
    INDEX idx_is_liked (is_liked)
);
```

---

## 使用示例

### 1. 完整的调查流程

```javascript
// 1. 初始化调查
const survey = new SurveySession()

// 2. 加载图片列表
const imageIds = await surveyAPI.getImageIds()
survey.setImages(imageIds)

// 3. 用户评分
survey.rateImage(1, true)   // 喜欢图片1
survey.rateImage(2, false)  // 不喜欢图片2

// 4. 提交调查
const result = await survey.submit()
```

---

### 2. 统计数据展示

```vue
<template>
  <div class="stats-dashboard">
    <!-- 总体统计 -->
    <div class="summary">
      <h2>总参与人数: {{ stats.totalResponses }}</h2>
    </div>
    
    <!-- 图片喜欢统计 -->
    <SimpleBarChart 
      :chart-data="formatLikesData(stats.likesByImage)"
      title="图片喜欢统计"
    />
    
    <!-- 设备类型分布 -->
    <SimplePieChart 
      :chart-data="formatDeviceData(stats.deviceTypeStats)"
      title="设备类型分布"
    />
  </div>
</template>

<script>
export default {
  async mounted() {
    this.stats = await surveyAPI.getDetailedStats()
  },
  methods: {
    formatLikesData(data) {
      return data.map(([imageId, count]) => ({
        label: `图片 ${imageId}`,
        value: count
      }))
    }
  }
}
</script>
```

---

### 3. 自定义音乐播放器

```vue
<template>
  <div>
    <MusicPlayer 
      :playlist="customPlaylist"
      :auto-play="true"
      @song-changed="handleSongChange"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      customPlaylist: [
        {
          title: 'MY ALL',
          artist: '浜崎あゆみ',
          url: 'https://music.163.com/song/media/outer/url?id=27901719.mp3',
          cover: 'http://p2.music.126.net/vKn4n26btSPAdpEgW7X61w==/109951164762304097.jpg'
        }
      ]
    }
  },
  methods: {
    handleSongChange(song) {
      console.log('当前播放:', song.title)
    }
  }
}
</script>
```

---

### 4. 错误处理和验证

```javascript
// 前端表单验证
const validateSurvey = (ratings) => {
  if (Object.keys(ratings).length !== 8) {
    throw new Error('请完成所有图片的评分')
  }
  
  for (const [imageId, isLiked] of Object.entries(ratings)) {
    if (typeof isLiked !== 'boolean') {
      throw new Error(`图片${imageId}的评分无效`)
    }
  }
  
  return true
}

// API调用错误处理
try {
  const result = await surveyAPI.submitSurvey(data)
  if (!result.data.success) {
    showError(result.data.message)
  }
} catch (error) {
  if (error.response?.status === 400) {
    showError('提交数据格式错误')
  } else if (error.response?.status === 500) {
    showError('服务器内部错误')
  } else {
    showError('网络连接失败')
  }
}
```

---

## 部署和维护

### 1. 快速启动脚本

```bash
# 启动后端服务
./start-backend.bat

# 启动前端服务  
./start-frontend.bat

# 完整系统启动
./quick-start.bat
```

### 2. Docker 部署

```bash
# 使用 docker-compose 启动
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

### 3. 性能优化建议

- **图片优化**: 使用适当的图片格式和尺寸
- **缓存策略**: 合理设置Redis缓存过期时间
- **数据库优化**: 定期分析查询性能和索引使用
- **前端优化**: 启用图片预加载和懒加载

---

## 联系和支持

如果您在使用过程中遇到问题，请查看项目的README文件或提交Issue。

**文档版本**: v1.0  
**最后更新**: 2024年1月  
**适用版本**: Spring Boot 3.2.0, Vue 3.5.17