<script setup>
import { RouterView } from 'vue-router'
import MusicPlayer from './components/MusicPlayer.vue'
import { preloadAllImages } from './utils/imagePreloader'
import { onMounted } from 'vue'

// 在应用挂载时预加载所有图片
onMounted(() => {
  preloadAllImages()
})
</script>

<template>
  <RouterView />
  <MusicPlayer />
</template>

<style>
/* 全局样式可以在这里添加 */
</style>
