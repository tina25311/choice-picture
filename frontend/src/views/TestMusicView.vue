<template>
  <div class="test-music-page">
    <div class="container">
      <h1>音乐播放器测试页面</h1>
      
      <div class="test-section">
        <h2>功能测试</h2>
        <div class="test-grid">
          <div class="test-card">
            <h3>🎵 播放控制</h3>
            <p>测试播放、暂停、上一首、下一首功能</p>
            <div class="test-status">
              <span class="status-label">状态:</span>
              <span class="status-value">请查看右下角播放器</span>
            </div>
          </div>
          
          <div class="test-card">
            <h3>📱 响应式设计</h3>
            <p>测试在不同屏幕尺寸下的显示效果</p>
            <div class="test-status">
              <span class="status-label">状态:</span>
              <span class="status-value">请调整浏览器窗口大小</span>
            </div>
          </div>
          
          <div class="test-card">
            <h3>🎨 界面交互</h3>
            <p>测试最小化、展开、音量控制等功能</p>
            <div class="test-status">
              <span class="status-label">状态:</span>
              <span class="status-value">请操作播放器界面</span>
            </div>
          </div>
          
          <div class="test-card">
            <h3>🖱️ 拖拽移动</h3>
            <p>测试最小化状态下拖拽移动播放器位置</p>
            <div class="test-status">
              <span class="status-label">状态:</span>
              <span class="status-value">在最小化状态下拖拽播放器</span>
            </div>
          </div>
          
          <div class="test-card">
            <h3>🔗 添加歌曲</h3>
            <p>测试添加网易云音乐链接功能</p>
            <div class="test-status">
              <span class="status-label">状态:</span>
              <span class="status-value">请点击播放器中的"添加歌曲"</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="test-section">
        <h2>示例链接</h2>
        <p>您可以使用以下网易云音乐链接进行测试：</p>
        <div class="example-links">
          <div class="link-item">
            <strong>MY ALL - 浜崎あゆみ</strong>
            <code>https://music.163.com/#/song?id=27901719</code>
          </div>
          <div class="link-item">
            <strong>起风了 - 买辣椒也用券</strong>
            <code>https://music.163.com/#/song?id=1330348068</code>
          </div>
          <div class="link-item">
            <strong>海阔天空 - Beyond</strong>
            <code>https://music.163.com/#/song?id=347230</code>
          </div>
          <div class="link-item">
            <strong>稻香 - 周杰伦</strong>
            <code>https://music.163.com/#/song?id=185809</code>
          </div>
        </div>
      </div>
      
      <div class="test-section">
        <h2>使用说明</h2>
        <ol class="instructions">
          <li>查看页面右下角的音乐播放器（默认折叠状态）</li>
          <li>音乐将自动开始播放（需要用户交互）</li>
          <li>在最小化状态下，可以拖拽播放器到任意位置</li>
          <li>点击播放器展开完整界面</li>
          <li>调节音量和进度条</li>
          <li>点击"添加歌曲"按钮添加新的网易云音乐</li>
          <li>在播放列表中选择不同的歌曲</li>
        </ol>
      </div>
      
      <div class="test-section">
        <h2>注意事项</h2>
        <div class="notice">
          <p>⚠️ 由于网络限制，某些网易云音乐链接可能无法正常播放</p>
          <p>💡 建议使用VPN或代理服务来确保音乐正常播放</p>
          <p>🔒 请遵守相关音乐版权法律法规</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 这个页面主要用于测试音乐播放器功能
</script>

<style scoped>
.test-music-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 2.5em;
}

.test-section {
  margin-bottom: 40px;
}

h2 {
  color: #667eea;
  border-bottom: 2px solid #667eea;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.test-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.test-card {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 20px;
  border-left: 4px solid #667eea;
}

.test-card h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.test-card p {
  color: #666;
  margin-bottom: 15px;
}

.test-status {
  display: flex;
  align-items: center;
  gap: 10px;
}

.status-label {
  font-weight: bold;
  color: #333;
}

.status-value {
  color: #667eea;
  font-style: italic;
}

.example-links {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 20px;
}

.link-item {
  margin-bottom: 15px;
  padding: 10px;
  background: white;
  border-radius: 5px;
  border-left: 3px solid #667eea;
}

.link-item strong {
  display: block;
  color: #333;
  margin-bottom: 5px;
}

.link-item code {
  background: #e9ecef;
  padding: 5px 8px;
  border-radius: 3px;
  font-size: 0.9em;
  color: #495057;
}

.instructions {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 20px 20px 20px 40px;
}

.instructions li {
  margin-bottom: 10px;
  color: #333;
  line-height: 1.6;
}

.notice {
  background: #fff3cd;
  border: 1px solid #ffeaa7;
  border-radius: 10px;
  padding: 20px;
}

.notice p {
  margin: 5px 0;
  color: #856404;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 20px;
    margin: 10px;
  }
  
  h1 {
    font-size: 2em;
  }
  
  .test-grid {
    grid-template-columns: 1fr;
  }
  
  .link-item code {
    font-size: 0.8em;
    word-break: break-all;
  }
}
</style> 