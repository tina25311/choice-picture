<template>
  <div class="survey-container">
    <!-- 进度条 -->
    <div class="progress-container">
      <div class="progress-bar">
        <div 
          class="progress-fill" 
          :style="{ width: `${(currentIndex + 1) / totalImages * 100}%` }"
        ></div>
      </div>
    </div>

    <!-- 图片显示区域 -->
    <div class="image-container" v-if="!isCompleted" 
         @touchstart="handleTouchStart" 
         @touchend="handleTouchEnd">
      <div class="image-wrapper" :class="{ 'image-visible': isImageVisible }">
        <img 
          v-if="currentImageElement"
          :src="currentImageElement.src"
          :alt="`Image ${currentImageId}`"
          class="survey-image"
          @contextmenu.prevent
          @dragstart.prevent
          @mousedown.prevent
        />
        
        <!-- 评分按钮 -->
        <div class="rating-buttons">
          <button 
            class="rating-button dislike-button" 
            :class="{ 'active': currentRating === false }" 
            @click="rateImage(false)"
            type="button"
          >
            <DislikeIcon :is-active="currentRating === false" />
            <span class="rating-text">{{ $t('survey.dislike') }}</span>
          </button>
          
          <button 
            class="rating-button like-button" 
            :class="{ 'active': currentRating === true }" 
            @click="rateImage(true)"
            type="button"
          >
            <LikeIcon :is-active="currentRating === true" />
            <span class="rating-text">{{ $t('survey.like') }}</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 提交按钮 -->
    <div class="submit-section" v-if="isCompleted && !isSubmitting">
      <button class="submit-button" @click="submitSurvey">
        提交问卷
      </button>
    </div>

    <!-- 加载状态 -->
    <div class="loading-overlay" v-if="isLoading">
      <div class="loading-content">
        <div class="loading-spinner"></div>
        <p>{{ $t('common.loading') }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import LikeIcon from '@/components/icons/LikeIcon.vue'
import DislikeIcon from '@/components/icons/DislikeIcon.vue'
import { surveyAPI } from '@/services/api'
import { imageMap as _imageMap } from '../assets/imageMap';
import { getPreloadedImage, isImagePreloaded } from '../utils/imagePreloader';

export default {
  name: 'SurveyView',
  components: {
    LikeIcon,
    DislikeIcon
  },
  data() {
    return {
      imageMap: _imageMap || {},
      imageIds: [],
      currentIndex: 0,
      ratings: {},
      currentRating: null,
      isImageVisible: false,
      isLoading: true,
      isSubmitting: false,
      isCompleted: false,
      touchStartX: 0,
      touchEndX: 0,
      currentImageElement: null
    }
  },
  computed: {
    totalImages() {
      return this.imageIds.length
    },
    currentImageId() {
      return Number(this.imageIds[this.currentIndex]) || 1
    }
  },
  async mounted() {
    console.log('imageMap:', this.imageMap);
    await this.loadImageIds()
  },
  methods: {
    async loadImageIds() {
      try {
        const response = await surveyAPI.getImageIds()
        this.imageIds = response.data
        this.isLoading = false
        // 预加载当前图片
        this.updateCurrentImageElement()
      } catch (error) {
        console.error('加载图片ID失败:', error)
        // 使用默认图片ID
        this.imageIds = [1, 2, 3, 4, 5, 6, 7, 8]
        this.isLoading = false
        // 预加载当前图片
        this.updateCurrentImageElement()
      }
    },

    updateCurrentImageElement() {
      const imageId = this.currentImageId
      if (isImagePreloaded(imageId)) {
        this.currentImageElement = getPreloadedImage(imageId)
        this.onImageLoad()
      }
    },
    
    handleTouchStart(e) {
      this.touchStartX = e.changedTouches[0].screenX
    },
    
    handleTouchEnd(e) {
      this.touchEndX = e.changedTouches[0].screenX
      this.handleSwipe()
    },
    
    handleSwipe() {
      const swipeThreshold = 50
      const diff = this.touchStartX - this.touchEndX
      
      if (Math.abs(diff) > swipeThreshold) {
        if (diff > 0) {
          // 向左滑动，下一张
          this.nextImage()
        } else {
          // 向右滑动，上一张（如果需要的话）
          // this.previousImage()
        }
      }
    },
    
    onImageLoad() {
      // 图片加载完成后显示动画
      setTimeout(() => {
        this.isImageVisible = true
      }, 100)
    },
    
    onImageError() {
      console.error('图片加载失败:', this.currentImageId)
      // 可以设置默认图片或错误处理
    },
    
    rateImage(isLiked) {
      console.log('rateImage called with:', isLiked)  // 添加日志
      console.log('Before - currentRating:', this.currentRating)  // 添加日志
      
      this.currentRating = isLiked
      this.ratings[this.currentImageId] = isLiked
      
      console.log('After - currentRating:', this.currentRating)  // 添加日志
      console.log('Ratings:', this.ratings)  // 添加日志
      
      // 自动下一张
      setTimeout(() => {
        this.nextImage()
      }, 300)
    },
    
    nextImage() {
      console.log('nextImage called - currentRating:', this.currentRating)
      if (this.currentRating === null) return
      
      this.isImageVisible = false
      
      setTimeout(() => {
        this.currentIndex++
        this.currentRating = null
        
        if (this.currentIndex >= this.totalImages) {
          this.isCompleted = true
        } else {
          // 更新当前显示的图片元素
          this.updateCurrentImageElement()
        }
      }, 300)
    },
    
    async submitSurvey() {
      if (Object.keys(this.ratings).length !== this.totalImages) {
        alert('请完成所有图片的评分')
        return
      }
      
      this.isSubmitting = true
      
      try {
        const surveyData = {
          imageRatings: Object.entries(this.ratings).map(([imageId, isLiked]) => ({
            imageId: parseInt(imageId),
            isLiked
          }))
        }
        
        const response = await surveyAPI.submitSurvey(surveyData)
        
        if (response.data.success) {
          this.$router.push('/completion')
        } else {
          alert('提交失败: ' + response.data.message)
        }
      } catch (error) {
        console.error('提交调查失败:', error)
        alert('提交失败，请重试')
      } finally {
        this.isSubmitting = false
      }
    }
  }
}
</script>

<style scoped>
.survey-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
}

.progress-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.9);
  padding: 0.5rem 1rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
  backdrop-filter: blur(10px);
}

.progress-bar {
  width: 100%;
  height: 6px;
  background: #e0e0e0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  transition: width 0.3s ease;
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding-top: 0;
}

.image-wrapper {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  opacity: 0;
  transform: scale(0.9);
  transition: all 0.5s ease;
}

.image-wrapper.image-visible {
  opacity: 1;
  transform: scale(1);
}

.survey-image {
  width: 100%;
  height: 100%;
  display: block;
  object-fit: cover;
}

.rating-buttons {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-around;
  padding: 2rem;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.5));
  gap: 2rem;
}

.rating-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: transparent;
  border: none;
  outline: none;
  min-width: 160px;
  min-height: 120px;
  backdrop-filter: blur(5px);
}

.rating-button:focus {
  outline: 2px solid rgba(255, 255, 255, 0.3);
  outline-offset: 2px;
}

.rating-text {
  font-weight: 600;
  color: rgba(255, 255, 255, 0.8);
  font-size: 1.1rem;
  transition: color 0.3s ease;
  pointer-events: none;
  white-space: nowrap;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.rating-button.active .rating-text {
  color: inherit;
}

.dislike-button {
  color: rgba(255, 255, 255, 0.8);
  transform: translateY(0);
  transition: all 0.3s ease;
}

.dislike-button:hover {
  background: rgba(255, 217, 0, 0.1);
  transform: translateY(5px);
  box-shadow: 0 5px 20px rgba(255, 217, 0, 0.2);
}

.dislike-button.active {
  color: #ffd600;
  background: rgba(255, 217, 0, 0.15);
  transform: translateY(5px);
  box-shadow: 0 5px 20px rgba(255, 217, 0, 0.3);
}

.like-button {
  color: rgba(255, 255, 255, 0.8);
  transform: translateY(0);
  transition: all 0.3s ease;
}

.like-button:hover {
  background: rgba(231, 76, 60, 0.1);
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(231, 76, 60, 0.2);
}

.like-button.active {
  color: #e74c3c;
  background: rgba(231, 76, 60, 0.15);
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(231, 76, 60, 0.3);
}

.submit-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;
}

.submit-button {
  background: linear-gradient(45deg, #ff6b6b, #ee5a24);
  color: white;
  border: none;
  padding: 1.5rem 3rem;
  font-size: 1.3rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.loading-content {
  text-align: center;
}

.loading-spinner {
  display: inline-block;
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 移动端样式调整 */
@media (max-width: 768px) {
  .survey-container {
    padding: 0;
  }
  
  .progress-container {
    padding: 0.3rem 0.5rem;
  }
  
  .image-container {
    padding-top: 0;
  }
  
  .image-wrapper {
    height: 100vh;
  }
  
  .rating-buttons {
    padding: 1.5rem;
    gap: 1rem;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
  }
  
  .rating-button {
    min-width: 120px;
    min-height: 100px;
    padding: 15px;
  }
  
  .rating-text {
    font-size: 1rem;
  }
  
  .submit-section {
    padding: 1rem;
  }
  
  .submit-button {
    padding: 1.2rem 2.5rem;
    font-size: 1.1rem;
  }
}

/* 触摸设备优化 */
@media (hover: none) {
  .rating-button {
    padding: 25px;
  }
  
  .rating-buttons {
    padding-bottom: 3rem;
  }
  
  .rating-button:active {
    transform: scale(0.95);
  }
}
</style> 