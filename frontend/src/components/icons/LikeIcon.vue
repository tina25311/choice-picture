<template>
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 24 24"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    class="like-icon"
    @click="$emit('click')"
  >
    <!-- 心形主体 -->
    <path
      d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
      :fill="isActive ? 'url(#likeGradient)' : '#f8f9fa'"
      :stroke="isActive ? '#e74c3c' : '#dee2e6'"
      stroke-width="1.5"
      stroke-linejoin="round"
    />
    
    <!-- 心形内部高光 -->
    <path
      d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
      :fill="isActive ? 'url(#likeHighlight)' : 'transparent'"
      opacity="0.3"
    />
    
    <!-- 渐变定义 -->
    <defs>
      <linearGradient id="likeGradient" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" style="stop-color:#e74c3c;stop-opacity:1" />
        <stop offset="100%" style="stop-color:#c0392b;stop-opacity:1" />
      </linearGradient>
      <linearGradient id="likeHighlight" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" style="stop-color:#ffffff;stop-opacity:0.8" />
        <stop offset="100%" style="stop-color:#ffffff;stop-opacity:0.2" />
      </linearGradient>
    </defs>
  </svg>
</template>

<script>
export default {
  name: 'LikeIcon',
  props: {
    size: {
      type: Number,
      default: 48
    },
    isActive: {
      type: Boolean,
      default: false
    }
  },
  emits: ['click']
}
</script>

<style scoped>
.like-icon {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.1));
}

.like-icon:hover {
  transform: scale(1.2) translateY(-3px);
  filter: drop-shadow(0 8px 20px rgba(231, 76, 60, 0.4));
}

.like-icon:active {
  transform: scale(0.9);
  transition: all 0.1s ease;
}

.like-icon path {
  transition: all 0.3s ease;
}

/* 添加心跳动画效果 */
.like-icon:hover path:first-child {
  animation: heartbeat 0.6s ease-in-out;
}

@keyframes heartbeat {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}
</style> 