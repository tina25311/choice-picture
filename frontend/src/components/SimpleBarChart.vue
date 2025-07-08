<template>
  <div class="simple-bar-chart">
    <div class="chart-container" ref="chartContainer">
      <div 
        v-for="(item, index) in chartData" 
        :key="index"
        class="bar-item"
        @click="handleBarClick(index)"
      >
        <div class="bar-label">{{ item.label }}</div>
        <div class="bar-wrapper">
          <div 
            class="bar" 
            :style="{ 
              height: `${(item.value / maxValue) * 100}%`,
              backgroundColor: barColor
            }"
          ></div>
        </div>
        <div class="bar-value">{{ item.value }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SimpleBarChart',
  props: {
    data: {
      type: Array,
      default: () => []
    },
    barColor: {
      type: String,
      default: 'rgba(75, 192, 192, 0.8)'
    }
  },
  data() {
    return {
      // 移除tooltip相关数据
    }
  },
  computed: {
    chartData() {
      if (!this.data || !Array.isArray(this.data) || this.data.length === 0) {
        return []
      }
      
      return this.data.map((item, index) => ({
        label: item && item[0] ? item[0] : `项目${index + 1}`,
        value: item && item[1] ? item[1] : 0
      }))
    },
    maxValue() {
      if (this.chartData.length === 0) return 1
      return Math.max(...this.chartData.map(item => item.value), 1)
    }
  },
  methods: {
    handleBarClick(index) {
      const item = this.chartData[index]
      if (item) {
        // 确保传递的是数字类型的图片ID
        this.$emit('bar-click', parseInt(item.label))
      }
    },
  }
}
</script>

<style scoped>
.simple-bar-chart {
  width: 100%;
  height: 100%;
  padding: 20px;
  position: relative;
}

.chart-container {
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 300px;
  gap: 10px;
}

.bar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  cursor: pointer;
  transition: all 0.3s ease;
}

.bar-item:hover {
  transform: translateY(-5px);
}

.bar-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 10px;
  text-align: center;
  word-break: break-word;
}

.bar-wrapper {
  width: 100%;
  height: 200px;
  display: flex;
  align-items: flex-end;
  margin-bottom: 10px;
}

.bar {
  width: 100%;
  min-height: 4px;
  border-radius: 4px 4px 0 0;
  transition: all 0.3s ease;
}

.bar-item:hover .bar {
  opacity: 0.8;
  transform: scaleY(1.05);
}

.bar-value {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

/* 图片缩略图tooltip样式 */
.image-tooltip {
  position: absolute;
  z-index: 1000;
  pointer-events: none;
  animation: tooltipFadeIn 0.2s ease-in-out;
}

.tooltip-content {
  background: white;
  border-radius: 8px;
  padding: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  border: 1px solid #e0e0e0;
  text-align: center;
  min-width: 120px;
}

.tooltip-image {
  width: 100px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 4px;
}

.tooltip-label {
  font-size: 11px;
  color: #666;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@keyframes tooltipFadeIn {
  from {
    opacity: 0;
    transform: translateY(10px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .tooltip-content {
    min-width: 100px;
  }
  
  .tooltip-image {
    width: 80px;
    height: 48px;
  }
  
  .tooltip-label {
    font-size: 10px;
  }
}
</style> 