<template>
  <div class="welcome-container">
    <div class="welcome-content" :class="{ 'animate-in': isVisible }">
      <div class="welcome-header">
        <h1 class="welcome-title">{{ $t('welcome.title') }}</h1>
        <div class="welcome-subtitle">{{ $t('welcome.subtitle') }}</div>
      </div>
      
      <div class="welcome-description">
        <p>{{ $t('welcome.description') }}</p>
      </div>
      
      <div class="welcome-features">
        <div class="feature-item">
          <div class="feature-icon">🎯</div>
          <div class="feature-text">简单快速</div>
        </div>
        <div class="feature-item">
          <div class="feature-icon">💡</div>
          <div class="feature-text">真实反馈</div>
        </div>
        <div class="feature-item">
          <div class="feature-icon">🚀</div>
          <div class="feature-text">即时结果</div>
        </div>
      </div>
      
      <div class="welcome-action">
        <button 
          class="start-button"
          @click="startSurvey"
          :disabled="isLoading"
        >
          <span v-if="!isLoading">{{ $t('welcome.startButton') }}</span>
          <span v-else class="loading-spinner"></span>
        </button>
      </div>
    </div>
    
    <div class="background-decoration">
      <div class="floating-shape shape-1"></div>
      <div class="floating-shape shape-2"></div>
      <div class="floating-shape shape-3"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WelcomeView',
  data() {
    return {
      isVisible: false,
      isLoading: false
    }
  },
  mounted() {
    // 延迟显示动画
    setTimeout(() => {
      this.isVisible = true
    }, 100)
  },
  methods: {
    startSurvey() {
      this.isLoading = true
      // 模拟加载时间
      setTimeout(() => {
        this.$router.push('/survey')
      }, 500)
    }
  }
}
</script>

<style scoped>
.welcome-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.welcome-content {
  text-align: center;
  color: white;
  max-width: 800px;
  padding: 2rem;
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.8s ease;
}

.welcome-content.animate-in {
  opacity: 1;
  transform: translateY(0);
}

.welcome-header {
  margin-bottom: 2rem;
}

.welcome-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.welcome-subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
  line-height: 1.6;
  margin-bottom: 1rem;
}

.welcome-description {
  margin-bottom: 3rem;
}

.welcome-description p {
  font-size: 1.1rem;
  opacity: 0.8;
  line-height: 1.6;
}

.welcome-features {
  display: flex;
  justify-content: center;
  gap: 3rem;
  margin-bottom: 3rem;
  flex-wrap: wrap;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
}

.feature-icon {
  font-size: 2rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.feature-text {
  font-size: 1rem;
  opacity: 0.9;
}

.welcome-action {
  margin-top: 2rem;
}

.start-button {
  background: linear-gradient(45deg, #ff6b6b, #ee5a24);
  color: white;
  border: none;
  padding: 1rem 3rem;
  font-size: 1.2rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
}

.start-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.start-button:active {
  transform: translateY(0);
}

.start-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid transparent;
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.floating-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 100px;
  height: 100px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.shape-3 {
  width: 80px;
  height: 80px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

@media (max-width: 768px) {
  .welcome-container {
    padding: 0;
  }
  
  .welcome-content {
    max-width: 100%;
    padding: 1rem;
  }
  
  .welcome-title {
    font-size: 2rem;
  }
  
  .welcome-subtitle {
    font-size: 1rem;
  }
  
  .welcome-features {
    gap: 2rem;
  }
  
  .start-button {
    padding: 0.8rem 2rem;
    font-size: 1rem;
  }
}
</style> 