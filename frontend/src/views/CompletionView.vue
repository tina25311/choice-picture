<template>
  <div class="completion-container">
    <div class="completion-content" :class="{ 'animate-in': isVisible }">
      <!-- 成功图标 -->
      <div class="success-icon">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="12" cy="12" r="10" stroke="#4CAF50" stroke-width="2" fill="none"/>
          <path d="M9 12l2 2 4-4" stroke="#4CAF50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      
      <!-- 标题和消息 -->
      <div class="completion-text">
        <h1 class="completion-title">{{ $t('completion.title') }}</h1>
        <h2 class="completion-subtitle">{{ $t('completion.subtitle') }}</h2>
        <p class="completion-message">{{ $t('completion.message') }}</p>
      </div>
      
      <!-- 装饰元素 -->
      <div class="decoration-elements">
        <div class="confetti confetti-1"></div>
        <div class="confetti confetti-2"></div>
        <div class="confetti confetti-3"></div>
        <div class="confetti confetti-4"></div>
        <div class="confetti confetti-5"></div>
        <div class="confetti confetti-6"></div>
        <div class="confetti confetti-7"></div>
        <div class="confetti confetti-8"></div>
      </div>
      
      <!-- 返回按钮 -->
      <div class="completion-action">
        <button class="back-button" @click="goHome">
          {{ $t('completion.backToHome') }}
        </button>
        <button class="stats-button" @click="viewStats">
          {{ $t('completion.viewStats') }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CompletionView',
  data() {
    return {
      isVisible: false
    }
  },
  mounted() {
    // 延迟显示动画
    setTimeout(() => {
      this.isVisible = true
    }, 100)
  },
  methods: {
    goHome() {
      this.$router.push('/')
    },
    viewStats() {
      this.$router.push('/stats')
    }
  }
}
</script>

<style scoped>
.completion-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.completion-content {
  text-align: center;
  color: white;
  max-width: 600px;
  padding: 2rem;
  opacity: 0;
  transform: translateY(30px) scale(0.9);
  transition: all 0.8s ease;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  justify-content: space-between;
}

.completion-content.animate-in {
  opacity: 1;
  transform: translateY(0) scale(1);
}

.success-icon {
  margin-bottom: 2rem;
  flex-shrink: 0;
}

.success-icon svg {
  width: 80px;
  height: 80px;
  animation: successBounce 0.6s ease-in-out 0.5s both;
}

@keyframes successBounce {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

.completion-text {
  margin-bottom: 3rem;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.completion-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  animation: slideInUp 0.8s ease 0.8s both;
}

.completion-subtitle {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1rem;
  opacity: 0.9;
  animation: slideInUp 0.8s ease 1s both;
}

.completion-message {
  font-size: 1.1rem;
  opacity: 0.8;
  line-height: 1.6;
  animation: slideInUp 0.8s ease 1.2s both;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.completion-action {
  animation: slideInUp 0.8s ease 1.4s both;
  margin-top: auto;
  padding-top: 2rem;
}

.back-button {
  background: linear-gradient(45deg, #ff6b6b, #ee5a24);
  color: white;
  border: none;
  padding: 1rem 2.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  margin-right: 10px;
}

.back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.back-button:active {
  transform: translateY(0);
}

.stats-button {
  background: linear-gradient(45deg, #4ecdc4, #45b7d1);
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

.stats-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.stats-button:active {
  transform: translateY(0);
}

.decoration-elements {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.confetti {
  position: absolute;
  width: 10px;
  height: 10px;
  background: #ffd700;
  animation: confettiFall 3s linear infinite;
}

.confetti-1 {
  left: 10%;
  animation-delay: 0s;
  background: #ff6b6b;
}

.confetti-2 {
  left: 20%;
  animation-delay: 0.5s;
  background: #4ecdc4;
}

.confetti-3 {
  left: 30%;
  animation-delay: 1s;
  background: #45b7d1;
}

.confetti-4 {
  left: 40%;
  animation-delay: 1.5s;
  background: #96ceb4;
}

.confetti-5 {
  left: 50%;
  animation-delay: 2s;
  background: #feca57;
}

.confetti-6 {
  left: 60%;
  animation-delay: 2.5s;
  background: #ff9ff3;
}

.confetti-7 {
  left: 70%;
  animation-delay: 3s;
  background: #54a0ff;
}

.confetti-8 {
  left: 80%;
  animation-delay: 3.5s;
  background: #5f27cd;
}

@keyframes confettiFall {
  0% {
    transform: translateY(-100vh) rotate(0deg);
    opacity: 1;
  }
  100% {
    transform: translateY(100vh) rotate(360deg);
    opacity: 0;
  }
}

/* 移动端样式调整 */
@media (max-width: 768px) {
  .completion-container {
    padding: 0;
  }
  
  .completion-content {
    max-width: 100%;
    padding: 1rem;
  }
  
  .completion-title {
    font-size: 2.5rem;
  }
  
  .completion-subtitle {
    font-size: 1.3rem;
  }
  
  .completion-message {
    font-size: 1rem;
  }
  
  .completion-action {
    padding-top: 1rem;
  }
  
  .back-button,
  .stats-button {
    padding: 0.8rem 2rem;
    font-size: 1rem;
    margin-bottom: 0.5rem;
  }
}
</style> 