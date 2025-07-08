<template>
  <div>
    <!-- 遮罩层，仅在最大化时显示 -->
    <div v-if="!isMinimized" class="player-mask" @click="minimizePlayer"></div>
    <div 
      class="music-player" 
      :class="{ 'player-minimized': isMinimized }" 
      :style="playerStyle"
      @click="enableAutoPlay"
    >
      <!-- 最小化状态 -->
      <div 
        v-if="isMinimized" 
        class="mini-player" 
        @mousedown="startDrag"
        @touchstart="startDrag"
      >
        <div class="mini-controls">
          <button @click.stop="togglePlay" class="control-btn">
            <i :class="isPlaying ? 'fas fa-pause' : 'fas fa-play'"></i>
          </button>
        </div>
        <div class="mini-info">
          <div class="mini-title">
            <div class="mini-title-text" :class="{ 'no-scroll': !needScroll(currentSong.title) }">
              {{ currentSong.title || '未播放' }}
            </div>
          </div>
          <div class="mini-artist">
            <div class="mini-artist-text" :class="{ 'no-scroll': !needScroll(currentSong.artist) }">
              {{ currentSong.artist || '未知歌手' }}
            </div>
          </div>
        </div>
      </div>

      <!-- 完整播放器 -->
      <div v-else class="full-player">
        <div class="player-header">
          <h3>音乐播放器</h3>
          <button @click="minimizePlayer" class="minimize-btn" title="最小化">
            <i class="fas fa-minus"></i>
          </button>
        </div>
        <div class="player-content">
          <!-- 歌曲信息 -->
          <div class="song-info">
            <div class="song-cover">
              <img :src="currentSong.cover || '/images/default-cover.jpg'" alt="封面" />
            </div>
            <div class="song-details">
              <h4>{{ currentSong.title || '未选择歌曲' }}</h4>
              <p>{{ currentSong.artist || '未知歌手' }}</p>
            </div>
          </div>

          <!-- 播放控制 -->
          <div class="controls">
            <button @click="prevSong" class="control-btn">
              <i class="fas fa-backward"></i>
            </button>
            <button @click="togglePlay" class="play-btn">
              <i :class="isPlaying ? 'fas fa-pause' : 'fas fa-play'"></i>
            </button>
            <button @click="nextSong" class="control-btn">
              <i class="fas fa-forward"></i>
            </button>
          </div>

          <!-- 进度条 -->
          <div class="progress-container">
            <div class="time-display">
              <span>{{ formatTime(currentTime) }}</span>
              <span>{{ formatTime(duration) }}</span>
            </div>
            <div class="progress-bar" @click="seekTo">
              <div class="progress-fill" :style="{ width: progressPercent + '%' }"></div>
            </div>
          </div>

          <!-- 音量控制 -->
          <div class="volume-control">
            <i class="fas fa-volume-up"></i>
            <input 
              type="range" 
              min="0" 
              max="100" 
              v-model="volume" 
              @input="updateVolume"
              class="volume-slider"
            />
          </div>

          <!-- 播放列表 -->
          <div class="playlist-section">
            <div class="playlist-header">
              <h4>播放列表</h4>
              <button @click="addSong" class="add-btn">
                <i class="fas fa-plus"></i> 添加歌曲
              </button>
            </div>
            <div class="playlist">
              <div 
                v-for="(song, index) in playlist" 
                :key="index"
                :class="['playlist-item', { 'active': currentIndex === index }]"
                @click="playSong(index)"
              >
                <span class="song-title">{{ song.title }}</span>
                <span class="song-artist">{{ song.artist }}</span>
              </div>
            </div>
          </div>

          <!-- 添加歌曲对话框 -->
          <div v-if="showAddDialog" class="add-dialog">
            <div class="dialog-content">
              <h4>添加网易云音乐</h4>
              <input 
                v-model="newSongUrl" 
                placeholder="输入网易云音乐链接或歌曲ID"
                class="song-input"
              />
              <div class="dialog-buttons">
                <button @click="confirmAdd" class="confirm-btn">添加</button>
                <button @click="cancelAdd" class="cancel-btn">取消</button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 隐藏的音频元素 -->
      <audio 
        ref="audioElement"
        @timeupdate="onTimeUpdate"
        @loadedmetadata="onLoadedMetadata"
        @ended="onEnded"
        @play="onPlay"
        @pause="onPause"
        @error="onError"
      ></audio>

      <!-- 消息提示 -->
      <div v-if="showMessage" :class="['message', `message-${messageType}`]">
        {{ messageText }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, computed, watch } from 'vue'

// 响应式数据
const isMinimized = ref(true) // 默认折叠状态
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(50)
const currentIndex = ref(0)
const showAddDialog = ref(false)
const newSongUrl = ref('')
const audioElement = ref(null)
const showMessage = ref(false)
const messageText = ref('')
const messageType = ref('info')

// 拖拽相关状态
const isDragging = ref(false)
const dragStartX = ref(0)
const dragStartY = ref(0)
const playerX = ref(20) // 初始X位置
const playerY = ref(20) // 初始Y位置
const dragDistance = ref(0) // 拖拽距离
const dragStartTime = ref(0) // 拖拽开始时间

// 播放列表
const playlist = reactive([
  {
    title: 'MY ALL',
    artist: '浜崎あゆみ',
    // url: 'https://m804.music.126.net/20250707101813/8918d0d69b532f0ab010736eaef4c8c2/jdyyaac/obj/w5rDlsOJwrLDjj7CmsOj/35939121252/4008/3859/8fc8/ee21bf66139337f5219ba18392164261.m4a',
    url: 'https://music.163.com/song/media/outer/url?id=27901719.mp3',
    cover: 'http://p2.music.126.net/vKn4n26btSPAdpEgW7X61w==/109951164762304097.jpg?param=130y130'
  }
])

// 当前歌曲
const currentSong = computed(() => {
  return playlist[currentIndex.value] || {}
})

// 进度百分比
const progressPercent = computed(() => {
  return duration.value > 0 ? (currentTime.value / duration.value) * 100 : 0
})

// 播放器样式
const playerStyle = computed(() => {
  const style = {
    left: `${playerX.value}px`,
    top: `${playerY.value}px`,
    position: 'fixed'
  }
  
  // 当播放器最大化时，确保有足够的空间显示
  if (!isMinimized.value) {
    // 检查是否靠近右边缘，如果是则调整位置
    const maxX = window.innerWidth - 350 // 播放器宽度
    if (playerX.value > maxX) {
      style.left = `${maxX}px`
      playerX.value = maxX
    }
    
    // 检查是否靠近下边缘，如果是则调整位置
    const maxY = window.innerHeight - 500 // 播放器高度
    if (playerY.value > maxY) {
      style.top = `${maxY}px`
      playerY.value = maxY
    }
  }
  
  return style
})

// 播放控制方法
const togglePlay = () => {
  if (isPlaying.value) {
    audioElement.value.pause()
  } else {
    audioElement.value.play()
  }
}

// 判断是否需要滚动
const needScroll = (text) => {
  if (!text) return false;
  const testDiv = document.createElement('div');
  testDiv.style.fontSize = '11px'; // 与 CSS 中的字体大小相同
  testDiv.style.position = 'absolute';
  testDiv.style.visibility = 'hidden';
  testDiv.innerText = text;
  document.body.appendChild(testDiv);
  const width = testDiv.offsetWidth;
  document.body.removeChild(testDiv);
  return width > 80; // 如果文本宽度超过容器宽度（90px - 内边距）则需要滚动
}

const prevSong = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--
    loadAndPlay()
  }
}

const nextSong = () => {
  if (currentIndex.value < playlist.length - 1) {
    currentIndex.value++
    loadAndPlay()
  }
}

const playSong = (index) => {
  currentIndex.value = index
  loadAndPlayWithAutoPlay() // 改用 loadAndPlayWithAutoPlay 而不是 loadAndPlay
}

const loadAndPlay = () => {
  if (playlist[currentIndex.value]) {
    audioElement.value.src = playlist[currentIndex.value].url
    audioElement.value.load()
    // 不在这里自动播放，让onMounted中的逻辑处理
  }
}

const loadAndPlayWithAutoPlay = () => {
  if (playlist[currentIndex.value]) {
    audioElement.value.src = playlist[currentIndex.value].url
    audioElement.value.load()
    // 尝试自动播放
    audioElement.value.play().catch(error => {
      console.log('自动播放失败:', error)
      showMessageFunc('点击播放按钮开始播放音乐', 'info')
    })
  }
}

// 进度控制
const seekTo = (event) => {
  const rect = event.target.getBoundingClientRect()
  const percent = (event.clientX - rect.left) / rect.width
  const newTime = percent * duration.value
  audioElement.value.currentTime = newTime
}

const updateVolume = () => {
  audioElement.value.volume = volume.value / 100
}

// 时间格式化
const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 音频事件处理
const onTimeUpdate = () => {
  currentTime.value = audioElement.value.currentTime
}

const onLoadedMetadata = () => {
  duration.value = audioElement.value.duration
}

const onEnded = () => {
  nextSong()
}

const onPlay = () => {
  isPlaying.value = true
}

const onPause = () => {
  isPlaying.value = false
}

const onError = () => {
  showMessageFunc('播放失败，请检查网络连接或歌曲链接', 'error')
}

// 拖拽相关方法
const startDrag = (event) => {
  if (!isMinimized.value) return // 只在最小化状态下允许拖拽
  
  isDragging.value = true
  dragDistance.value = 0
  dragStartTime.value = Date.now()
  
  const clientX = event.clientX || event.touches[0].clientX
  const clientY = event.clientY || event.touches[0].clientY
  
  dragStartX.value = clientX - playerX.value
  dragStartY.value = clientY - playerY.value
  
  // 添加事件监听器
  document.addEventListener('mousemove', onDrag)
  document.addEventListener('touchmove', onDrag, { passive: false })
  document.addEventListener('mouseup', stopDrag)
  document.addEventListener('touchend', stopDrag)
  
  // 防止文本选择
  event.preventDefault()
}

const onDrag = (event) => {
  if (!isDragging.value) return
  
  const clientX = event.clientX || event.touches[0].clientX
  const clientY = event.clientY || event.touches[0].clientY
  
  // 计算拖拽距离
  const deltaX = clientX - (dragStartX.value + playerX.value)
  const deltaY = clientY - (dragStartY.value + playerY.value)
  dragDistance.value = Math.sqrt(deltaX * deltaX + deltaY * deltaY)
  
  // 计算新位置
  let newX = clientX - dragStartX.value
  let newY = clientY - dragStartY.value
  
  // 边界检查，确保播放器不会完全移出屏幕
  const maxX = window.innerWidth - 90 // 播放器宽度
  const maxY = window.innerHeight - 90 // 播放器高度
  
  newX = Math.max(0, Math.min(newX, maxX))
  newY = Math.max(0, Math.min(newY, maxY))
  
  playerX.value = newX
  playerY.value = newY
  
  event.preventDefault()
}

const stopDrag = () => {
  const dragEndTime = Date.now()
  const dragDuration = dragEndTime - dragStartTime.value
  
  // 判断是否为点击（拖拽距离小于5px且时间小于200ms）
  const isClick = dragDistance.value < 5 && dragDuration < 200
  
  isDragging.value = false
  
  // 移除事件监听器
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('touchmove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
  document.removeEventListener('touchend', stopDrag)
  
  // 如果是点击，触发最大化
  if (isClick && isMinimized.value) {
    toggleMinimize()
  }
}

// 界面控制
const toggleMinimize = () => {
  isMinimized.value = !isMinimized.value
}

// 最小化播放器
const minimizePlayer = () => {
  isMinimized.value = true
}

// 处理点击外部区域
const handleClickOutside = (event) => {
  // 检查点击是否在播放器外部
  const playerElement = event.target.closest('.music-player')
  if (!playerElement && !isMinimized.value) {
    minimizePlayer()
  }
}

const addSong = () => {
  showAddDialog.value = true
}

const confirmAdd = () => {
  if (newSongUrl.value.trim()) {
    // 解析网易云音乐链接或ID
    const songId = extractSongId(newSongUrl.value)
    if (songId) {
      const newSong = {
        title: `歌曲 ${playlist.length + 1}`,
        artist: '未知歌手',
        url: `https://music.163.com/song/media/outer/url?id=${songId}.mp3`,
        cover: 'https://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg'
      }
      playlist.push(newSong)
      newSongUrl.value = ''
      showAddDialog.value = false
      showMessageFunc('歌曲添加成功！', 'success')
    } else {
      showMessageFunc('无法解析网易云音乐链接，请检查格式', 'error')
    }
  } else {
    showMessageFunc('请输入有效的网易云音乐链接', 'error')
  }
}

const showMessageFunc = (text, type = 'info') => {
  messageText.value = text
  messageType.value = type
  showMessage.value = true
  setTimeout(() => {
    showMessage.value = false
  }, 3000)
}

// 用户交互检测
const hasUserInteracted = ref(false)
const enableAutoPlay = () => {
  if (!hasUserInteracted.value) {
    hasUserInteracted.value = true
    // 用户首次交互后尝试播放
    if (audioElement.value && !isPlaying.value) {
      audioElement.value.play().catch(error => {
        console.log('用户交互后播放失败:', error)
      })
    }
  }
}

const cancelAdd = () => {
  newSongUrl.value = ''
  showAddDialog.value = false
}

// 解析网易云音乐ID
const extractSongId = (url) => {
  // 支持多种格式的网易云音乐链接
  const patterns = [
    /music\.163\.com\/#\/song\?id=(\d+)/,
    /music\.163\.com\/song\?id=(\d+)/,
    /music\.163\.com\/song\/(\d+)/,
    /^(\d+)$/
  ]
  
  for (const pattern of patterns) {
    const match = url.match(pattern)
    if (match) {
      return match[1]
    }
  }
  return null
}

// 监听播放器最大化/最小化，动态设置body样式，防止页面滚动
watch(isMinimized, (val) => {
  if (!val) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = ''
  }
})

// 生命周期
onMounted(() => {
  // 初始化音频元素
  if (audioElement.value) {
    audioElement.value.volume = volume.value / 100
  }
  
  // 加载第一首歌并自动播放
  if (playlist.length > 0) {
    loadAndPlayWithAutoPlay()
  }
  if (!isMinimized.value) {
    document.body.style.overflow = 'hidden'
  }
})

onUnmounted(() => {
  if (audioElement.value) {
    audioElement.value.pause()
  }
  
  // 清理拖拽事件监听器
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('touchmove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
  document.removeEventListener('touchend', stopDrag)
  document.body.style.overflow = ''
})
</script>

<style scoped>
.player-mask {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.15);
  z-index: 999;
}
.music-player {
  position: fixed;
  z-index: 1000;
  transition: all 0.3s ease;
  pointer-events: none;
}
.full-player,
.mini-player {
  pointer-events: auto;
}
.player-minimized {
  width: 90px;
}
.full-player {
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  width: 350px;
  max-height: 500px;
  overflow: hidden;
}
.mini-player {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 8px 6px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  cursor: grab;
  transition: all 0.3s ease;
  height: 90px;
  box-sizing: border-box;
  gap: 4px;
  user-select: none;
}

.mini-player:active {
  cursor: grabbing;
}

.mini-player:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.mini-controls {
  margin-bottom: 2px;
  width: 100%;
  display: flex;
  justify-content: center;
}

.mini-controls .control-btn {
  padding: 4px;
  font-size: 16px; /* 增大按钮图标大小 */
  background: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  width: 28px; /* 增大按钮尺寸 */
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 隐藏下一首按钮 */
.mini-controls .control-btn:last-child {
  display: none;
}

.mini-controls .control-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.mini-info {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.mini-title, .mini-artist {
  width: 100%;
  position: relative;
  height: 18px; /* 增加高度以适应更大的字体 */
  overflow: hidden;
}

.mini-title-text, .mini-artist-text {
  position: absolute;
  white-space: nowrap;
  animation: marquee 10s linear infinite;
  padding-left: 90px;
  will-change: transform;
}

.mini-title-text {
  font-size: 13px; /* 增大标题字体 */
  font-weight: 600;
}

.mini-artist-text {
  font-size: 11px; /* 增大艺术家名字体 */
  opacity: 0.9; /* 增加透明度，使文字更清晰 */
}

@keyframes marquee {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-100%);
  }
}

/* 当文字不需要滚动时的样式 */
.no-scroll {
  animation: none;
  padding-left: 0;
  text-align: center;
  position: static;
}

.full-player {
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  width: 350px;
  max-height: 500px;
  overflow: hidden;
}

.player-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.player-header h3 {
  margin: 0;
  font-size: 16px;
}

.minimize-btn {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 16px;
}

.player-content {
  padding: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.song-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.song-cover {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 15px;
}

.song-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.song-details h4 {
  margin: 0 0 5px 0;
  font-size: 16px;
}

.song-details p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.control-btn {
  background: none;
  border: none;
  font-size: 18px;
  color: #333;
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.control-btn:hover {
  background: #f0f0f0;
}

.play-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  padding: 12px;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.play-btn:hover {
  transform: scale(1.1);
}

.progress-container {
  margin-bottom: 20px;
}

.time-display {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
}

.progress-bar {
  width: 100%;
  height: 4px;
  background: #e0e0e0;
  border-radius: 2px;
  cursor: pointer;
  position: relative;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
  transition: width 0.1s ease;
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.volume-slider {
  flex: 1;
  height: 4px;
  border-radius: 2px;
  background: #e0e0e0;
  outline: none;
  -webkit-appearance: none;
}

.volume-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #667eea;
  cursor: pointer;
}

.playlist-section {
  border-top: 1px solid #e0e0e0;
  padding-top: 15px;
}

.playlist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.playlist-header h4 {
  margin: 0;
  font-size: 14px;
}

.add-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  cursor: pointer;
}

.playlist {
  max-height: 150px;
  overflow-y: auto;
}

.playlist-item {
  padding: 8px 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.2s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
  user-select: none;
}

.playlist-item:hover {
  background: #f5f5f5;
}

.playlist-item.active {
  background: #e3f2fd;
  color: #1976d2;
}

.song-title, .song-artist {
  pointer-events: none;
}

.song-title {
  font-size: 13px;
  font-weight: 500;
}

.song-artist {
  font-size: 12px;
  color: #666;
}

.add-dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1001;
}

.dialog-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 300px;
}

.dialog-content h4 {
  margin: 0 0 15px 0;
}

.song-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 15px;
}

.dialog-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.confirm-btn, .cancel-btn {
  padding: 8px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.confirm-btn {
  background: #667eea;
  color: white;
}

.cancel-btn {
  background: #f0f0f0;
  color: #333;
}

/* 消息提示样式 */
.message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 8px;
  color: white;
  font-size: 14px;
  z-index: 1002;
  animation: slideIn 0.3s ease;
}

.message-success {
  background: #4caf50;
}

.message-error {
  background: #f44336;
}

.message-info {
  background: #2196f3;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .music-player {
    bottom: 10px;
    right: 10px;
    left: 10px;
  }
  
  .full-player {
    width: 100%;
    max-width: 350px;
  }
  
  .mini-player {
    width: 100%;
  }
  
  .message {
    right: 10px;
    left: 10px;
  }
}
</style> 