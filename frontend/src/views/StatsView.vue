<template>
  <div class="stats-container">
    <div class="stats-header">
      <h1 class="stats-title">{{ $t('stats.title') }}</h1>
      <p class="stats-subtitle">{{ $t('stats.subtitle') }}</p>
    </div>

    <!-- 加载状态 -->
    <div class="loading-overlay" v-if="isLoading">
      <div class="loading-content">
        <div class="loading-spinner"></div>
        <p>{{ $t('common.loading') }}</p>
      </div>
    </div>

    <!-- 统计内容 -->
    <div class="stats-content" v-else>
      <!-- 总体统计 -->
      <div class="overview-section">
        <div class="stat-card">
          <div class="stat-number">{{ totalResponses }}</div>
          <div class="stat-label">{{ $t('stats.totalResponses') }}</div>
        </div>
      </div>

          <!-- 图片喜欢统计 -->
    <div class="chart-section">
      <h2 class="chart-title">{{ $t('stats.likesChart') }}</h2>
      <div class="chart-container">
        <SimpleBarChart 
          v-if="!isLoading"
          :data="likesByImage"
          bar-color="rgba(75, 192, 192, 0.8)"
          @bar-click="showImagePreview"
        />
        <div v-else class="chart-placeholder">
          <p>加载图表数据中...</p>
        </div>
      </div>
    </div>

          <!-- 图片不喜欢统计 -->
    <div class="chart-section">
      <h2 class="chart-title">{{ $t('stats.dislikesChart') }}</h2>
      <div class="chart-container">
        <SimpleBarChart 
          v-if="!isLoading"
          :data="dislikesByImage"
          bar-color="rgba(255, 99, 132, 0.8)"
          @bar-click="showImagePreview"
        />
        <div v-else class="chart-placeholder">
          <p>加载图表数据中...</p>
        </div>
      </div>
    </div>

      <!-- 终端类型统计 -->
      <div class="chart-section">
        <h2 class="chart-title">{{ $t('stats.deviceTypeChart') }}</h2>
        <div class="chart-container">
          <SimplePieChart 
            v-if="!isLoading"
            :data="deviceTypeStats"
          />
          <div v-else class="chart-placeholder">
            <p>加载图表数据中...</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 图片预览模态框（仅图片，无白边、无关闭按钮，遮罩透明） -->
    <div class="image-modal" v-if="showImageModal" @click="closeImageModal">
      <img v-if="imageMap[selectedImageId]" :src="imageMap[selectedImageId]" :alt="`Image ${selectedImageId}`" class="preview-image" @click.stop />
    </div>

    <!-- 返回按钮 -->
    <div class="stats-actions">
      <button class="back-button" @click="goBack">
        {{ $t('stats.backToHome') }}
      </button>
    </div>
  </div>
</template>

<script>
import { surveyAPI } from '@/services/api'
import SimpleBarChart from '@/components/SimpleBarChart.vue'
import SimplePieChart from '@/components/SimplePieChart.vue'
import { imageMap as _imageMap } from '../assets/imageMap';

export default {
  name: 'StatsView',
  components: {
    SimpleBarChart,
    SimplePieChart
  },
  data() {
    return {
      imageMap: _imageMap || {},
      isLoading: true,
      totalResponses: 0,
      likesByImage: [],
      dislikesByImage: [],
      deviceTypeStats: [],
      showImageModal: false,
      selectedImageId: null
    }
  },

  async mounted() {
    console.log('StatsView组件挂载')
    await this.loadStats()
    console.log('StatsView数据加载完成')
  },
  methods: {
    async loadStats() {
      try {
        const response = await surveyAPI.getDetailedStats()
        const data = response.data
        
        this.totalResponses = data.totalResponses || 0
        this.likesByImage = data.likesByImage || []
        this.dislikesByImage = data.dislikesByImage || []
        this.deviceTypeStats = data.deviceTypeStats || []
        
        this.isLoading = false
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // 确保在错误情况下也初始化数据为空数组
        this.totalResponses = 0
        this.likesByImage = []
        this.dislikesByImage = []
        this.deviceTypeStats = []
        this.isLoading = false
      }
    },
    

    
    showImagePreview(imageId) {
      const id = parseInt(imageId)
      console.log('预览图片ID:', id)
      if (!isNaN(id)) {
        this.selectedImageId = id
        this.showImageModal = true
      }
    },
    
    closeImageModal() {
      this.showImageModal = false
      this.selectedImageId = null
    },
    
    goBack() {
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.stats-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 2rem;
}

.stats-header {
  text-align: center;
  margin-bottom: 3rem;
}

.stats-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.stats-subtitle {
  font-size: 1.2rem;
  color: #7f8c8d;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-content {
  text-align: center;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.overview-section {
  margin-bottom: 3rem;
}

.stat-card {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  text-align: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  max-width: 300px;
  margin: 0 auto;
}

.stat-number {
  font-size: 3rem;
  font-weight: 700;
  color: #3498db;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 1.1rem;
  color: #7f8c8d;
}

.chart-section {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.chart-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 1.5rem;
  text-align: center;
}

.chart-container {
  height: 400px;
  position: relative;
}

.chart-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f8f9fa;
  border-radius: 10px;
  color: #6c757d;
  font-size: 1.1rem;
}

.stats-actions {
  text-align: center;
  margin-top: 3rem;
  padding-bottom: 2rem;
}

.back-button {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 1rem 2.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.image-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: transparent;
  border-radius: 0;
  max-width: 90%;
  max-height: 90%;
  overflow: hidden;
  box-shadow: none;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #2c3e50;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #7f8c8d;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-button:hover {
  color: #2c3e50;
}

.modal-body {
  padding: 0;
  background: transparent;
  text-align: center;
}

.preview-image {
  max-width: 90vw;
  max-height: 600px;
  border-radius: 10px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.35);
}

@media (max-width: 768px) {
  .stats-container {
    padding: 1rem;
  }
  
  .stats-title {
    font-size: 2rem;
  }
  
  .chart-container {
    height: 300px;
  }
  
  .modal-content {
    max-width: 95%;
  }
  
  .stats-actions {
    margin-top: 6rem;
    padding-bottom: 4rem;
  }
  
  .back-button {
    padding: 0.8rem 2rem;
    font-size: 1rem;
  }
}
</style> 