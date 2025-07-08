<template>
  <div class="simple-pie-chart">
    <div class="chart-container" ref="chartContainer">
      <div class="pie-wrapper">
        <svg :width="size" :height="size" class="pie-svg">
          <circle
            cx="50%"
            cy="50%"
            :r="radius"
            fill="none"
            stroke-width="0"
          />
          <g v-for="(slice, index) in pieSlices" :key="index">
            <path
              :d="slice.path"
              :fill="slice.color"
              @click="handleSliceClick(index)"
              class="pie-slice"
            />
          </g>
        </svg>
      </div>
      <div class="legend">
        <div 
          v-for="(item, index) in chartData" 
          :key="index"
          class="legend-item"
          @click="handleSliceClick(index)"
        >
          <div class="legend-color" :style="{ backgroundColor: colors[index] }"></div>
          <div class="legend-label">{{ item.label }}</div>
          <div class="legend-value">{{ item.value }} ({{ item.percentage }}%)</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SimplePieChart',
  props: {
    data: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      size: 300,
      colors: [
        'rgba(255, 99, 132, 0.8)',
        'rgba(54, 162, 235, 0.8)',
        'rgba(255, 205, 86, 0.8)',
        'rgba(75, 192, 192, 0.8)',
        'rgba(153, 102, 255, 0.8)',
        'rgba(255, 159, 64, 0.8)',
        'rgba(199, 199, 199, 0.8)',
        'rgba(83, 102, 255, 0.8)'
      ]
    }
  },
  computed: {
    chartData() {
      if (!this.data || !Array.isArray(this.data) || this.data.length === 0) {
        return []
      }
      
      const total = this.data.reduce((sum, item) => sum + (item && item[1] ? item[1] : 0), 0)
      
      return this.data.map((item, index) => ({
        label: item && item[0] ? item[0] : `项目${index + 1}`,
        value: item && item[1] ? item[1] : 0,
        percentage: total > 0 ? ((item && item[1] ? item[1] : 0) / total * 100).toFixed(1) : '0'
      }))
    },
    radius() {
      return this.size / 2 - 20
    },
    pieSlices() {
      if (this.chartData.length === 0) return []
      
      const total = this.chartData.reduce((sum, item) => sum + item.value, 0)
      if (total === 0) return []
      
      const slices = []
      let currentAngle = -Math.PI / 2 // 从顶部开始
      
      this.chartData.forEach((item, index) => {
        const sliceAngle = (item.value / total) * 2 * Math.PI
        const endAngle = currentAngle + sliceAngle
        
        const x1 = this.size / 2 + this.radius * Math.cos(currentAngle)
        const y1 = this.size / 2 + this.radius * Math.sin(currentAngle)
        const x2 = this.size / 2 + this.radius * Math.cos(endAngle)
        const y2 = this.size / 2 + this.radius * Math.sin(endAngle)
        
        const largeArcFlag = sliceAngle > Math.PI ? 1 : 0
        
        const path = [
          `M ${this.size / 2} ${this.size / 2}`,
          `L ${x1} ${y1}`,
          `A ${this.radius} ${this.radius} 0 ${largeArcFlag} 1 ${x2} ${y2}`,
          'Z'
        ].join(' ')
        
        slices.push({
          path,
          color: this.colors[index % this.colors.length]
        })
        
        currentAngle = endAngle
      })
      
      return slices
    }
  },
  methods: {
    handleSliceClick(index) {
      const item = this.chartData[index]
      if (item) {
        this.$emit('slice-click', item.label)
      }
    }
  }
}
</script>

<style scoped>
.simple-pie-chart {
  width: 100%;
  height: 100%;
  padding: 20px;
}

.chart-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.pie-wrapper {
  position: relative;
}

.pie-svg {
  cursor: pointer;
}

.pie-slice {
  cursor: pointer;
  transition: all 0.3s ease;
}

.pie-slice:hover {
  opacity: 0.8;
  transform: scale(1.02);
}

.legend {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 300px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 5px;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.legend-item:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.legend-color {
  width: 20px;
  height: 20px;
  border-radius: 3px;
  flex-shrink: 0;
}

.legend-label {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.legend-value {
  font-size: 12px;
  color: #666;
  font-weight: bold;
}
</style> 