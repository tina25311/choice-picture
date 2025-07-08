<template>
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 24 24"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    class="dislike-icon"
  >
    <!-- 破碎的心形主体 -->
    <path
      d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
      :fill="isActive ? 'url(#dislikeGradientYellow)' : '#f8f9fa'"
      :stroke="isActive ? '#ffd600' : '#dee2e6'"
      stroke-width="1.5"
      stroke-linejoin="round"
      opacity="0.6"
    />
    
    <!-- 心形裂缝 -->
    <path
      d="M12 8L10 6M12 8L14 6M12 8V12M10 10L8 8M14 10L16 8"
      :stroke="isActive ? '#ffd600' : '#adb5bd'"
      stroke-width="2"
      stroke-linecap="round"
      stroke-linejoin="round"
    />
    
    <!-- 心形内部阴影 -->
    <path
      d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
      :fill="isActive ? 'url(#dislikeShadowYellow)' : 'transparent'"
      opacity="0.4"
    />
    
    <!-- 渐变定义 -->
    <defs>
      <linearGradient id="dislikeGradientYellow" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" style="stop-color:#ffe066;stop-opacity:1" />
        <stop offset="100%" style="stop-color:#ffd600;stop-opacity:1" />
      </linearGradient>
      <linearGradient id="dislikeShadowYellow" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" style="stop-color:#ffd600;stop-opacity:0.6" />
        <stop offset="100%" style="stop-color:#ffe066;stop-opacity:0.3" />
      </linearGradient>
    </defs>
  </svg>
</template>

<script>
export default {
  name: 'DislikeIcon',
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
.dislike-icon {
  pointer-events: none; /* 添加这行，确保图标不会捕获点击事件 */
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.1));
}

.dislike-icon:hover {
  transform: scale(1.2) translateY(3px);
  filter: drop-shadow(0 8px 20px rgba(108, 117, 125, 0.4));
}

.dislike-icon:active {
  transform: scale(0.9);
  transition: all 0.1s ease;
}

.dislike-icon path {
  transition: all 0.3s ease;
}

/* 添加破碎动画效果 */
.dislike-icon:hover path:first-child {
  animation: breakHeart 0.6s ease-in-out;
}

@keyframes breakHeart {
  0%, 100% { transform: scale(1) rotate(0deg); }
  25% { transform: scale(1.05) rotate(-2deg); }
  75% { transform: scale(1.05) rotate(2deg); }
}
</style> 